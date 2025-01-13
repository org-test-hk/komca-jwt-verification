package kr.or.komca.foundation.jwt.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
//import kr.or.komca.foundation.jwt.common.response.CommonResponseFoundation;
import kr.or.komca.foundation.jwt.dto.TokenValidationResult;
import kr.or.komca.foundation.jwt.exception.filterException.InvalidTokenException;
import kr.or.komca.foundation.jwt.exception.filterException.MissingFingerprintException;
import kr.or.komca.foundation.jwt.exception.filterException.MissingTokenException;
import kr.or.komca.foundation.jwt.exception.filterException.TokenValidationException;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import kr.or.komca.foundation.jwt.security.config.SecurityURLConstants;
import kr.or.komca.foundation.jwt.security.constants.SecurityConstants;
import kr.or.komca.foundation.jwt.security.jwt.JwtTokenProvider;
import kr.or.komca.foundation.jwt.service.command.TokenCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


// 대빵 필터 -> 얘를 통해서 인증됨.

/**
 * JWT 인증 필터
 * Spring Security 필터 체인에서 JWT 토큰을 검증하고 인증 정보를 설정하는 필터
 *
 * <p>주요 기능:</p>
 * <ul>
 *   <li>JWT 토큰 추출 및 검증</li>
 *   <li>클라이언트 보안 정보(Fingerprint) 검증</li>
 *   <li>인증 정보 설정</li>
 *   <li>토큰 관련 이상 징후 로깅</li>
 * </ul>
 *
 * @author KomcaAuth Team
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider tokenProvider;
    private final TokenCommandService tokenCommandService;
    private final AuthenticationLogger authLogger;  // 로깅 전담 클래스

    /**
     * 인증을 건너뛸 Public URL 목록
     * 이 URL들에 대해서는 JWT 토큰 검증을 수행하지 않음
     */
    private final List<String> SKIP_URLS = Arrays.asList(SecurityURLConstants.PUBLIC_URLS);

    /**
     * JWT 인증 필터의 핵심 로직을 구현
     * 모든 요청에 대해 JWT 토큰을 검증하고 인증 정보를 설정
     *
     * @param request 현재 HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 처리 중 오류 발생시
     * @throws IOException I/O 처리 중 오류 발생시
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 필터를 건너뛰어야 하는 경우 처리
            if (shouldSkipFilter(request)) {
                filterChain.doFilter(request, response);
                return;
            }

            processAuthentication(request);
            filterChain.doFilter(request, response);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            throw e;  // 예외를 상위로 전파
        }
    }

    private void processAuthentication(HttpServletRequest request) {
        // JWT 토큰 추출
        String jwt = resolveToken(request);
        if (!StringUtils.hasText(jwt)) {
            authLogger.logMissingToken(request);
            throw new MissingTokenException();
        }

        // Fingerprint 검증
        String fingerprint = request.getHeader(SecurityConstants.HEADER_FINGERPRINT);
        if (!StringUtils.hasText(fingerprint)) {
            authLogger.logMissingFingerprint(request, jwt);
            tokenCommandService.logAbnormalAccess(jwt, SecurityConstants.EVENT_MISSING_FINGERPRINT, request);
            throw new MissingFingerprintException();
        }

        // JWT 서명 검증
        if (!tokenProvider.validateToken(jwt)) {
            authLogger.logInvalidToken(request, jwt);
            throw new InvalidTokenException();
        }

        // 비즈니스 규칙 검증 (블랙리스트, Fingerprint, IP 등)
        TokenValidationResult validationResult = tokenCommandService.validateToken(jwt, request);
        if (!validationResult.isValid()) {
            authLogger.logValidationFailure(request, jwt, validationResult);
            throw new TokenValidationException(validationResult.getErrorMessage());
        }

        // 경고가 있는 경우 로깅 (IP 변경 등)
        if (!validationResult.getWarnings().isEmpty()) {
            authLogger.logValidationWarnings(jwt, validationResult.getWarnings());
        }

        // 인증 정보 설정
        Authentication authentication = tokenProvider.getAuthentication(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        authLogger.logSuccessfulAuthentication(request, authentication);
    }

    /**
     * 현재 요청이 필터를 건너뛰어야 하는지 확인
     * Public URL에 대해서는 인증을 건너뜀
     *
     * @param request HTTP 요청
     * @return 필터를 건너뛸지 여부
     */
    private boolean shouldSkipFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return SKIP_URLS.stream().anyMatch(skipUrl ->
                path.startsWith(skipUrl) || path.matches(skipUrl.replace("/**", ".*"))
        );

    }

    /**
     * HTTP 요청의 Authorization 헤더에서 JWT 토큰을 추출
     * Bearer 타입의 토큰만 처리
     *
     * @param request HTTP 요청
     * @return 추출된 JWT 토큰, 없거나 잘못된 형식인 경우 null
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}