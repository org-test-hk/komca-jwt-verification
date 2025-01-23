package kr.or.komca.foundation.jwt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kr.or.komca.foundation.jwt.global.jwt.JwtProperties;
import kr.or.komca.foundation.jwt.global.jwt.constants.SecurityConstants;
import kr.or.komca.foundation.jwt.service.command.TokenVerificationCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
/**
 * JWT 토큰 검증을 담당하는 클래스
 * - 토큰 검증
 * - 인증 정보 추출
 * - 검증 실패 로깅
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenValidator {
    private final JwtProperties jwtProperties;
    private final TokenVerificationCommandService tokenCommandService;
    private Key key;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = (SecretKey) Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * JWT 토큰에서 인증 정보를 추출.
     *
     * @param token JWT 토큰
     * @return Spring Security Authentication 객체
     */
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        Collection<? extends GrantedAuthority> authorities =
                ((List<String>) claims.get("roles")).stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        UserDetails principal = User.builder()
                .username(claims.getSubject())
                .password("")
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * 토큰의 유효성을 검증.
     * JWT 서명 검증 및 만료 여부만 확인
     *
     * @param token 검증할 JWT 토큰
     * @return 유효성 여부
     */
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("잘못된 JWT 서명입니다: {}", e.getMessage());
            logTokenError(token, SecurityConstants.EVENT_INVALID_SIGNATURE);
        } catch (ExpiredJwtException e) {
            log.error("만료된 JWT 토큰입니다: {}", e.getMessage());
            logTokenError(token, SecurityConstants.EVENT_TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            log.error("지원되지 않는 JWT 토큰입니다: {}", e.getMessage());
            logTokenError(token, SecurityConstants.EVENT_UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            log.error("JWT 토큰이 잘못되었습니다: {}", e.getMessage());
            logTokenError(token, SecurityConstants.EVENT_INVALID_TOKEN);
        }
        return false;
    }

    /**
     * 토큰에서 Claims를 추출.
     *
     * @param token JWT 토큰
     * @return Claims 객체
     */
    protected Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey)key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * 토큰의 만료 시간 추출
     * @param token JWT 토큰
     * @return 만료 시간
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaims(token).getExpiration();
    }

    /**
     * 토큰 관련 에러를 로깅.
     *
     * @param token JWT 토큰
     * @param eventType 에러 이벤트 유형
     */
    private void logTokenError(String token, String eventType) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        tokenCommandService.logAbnormalAccess(token, eventType, request);
    }


    public String signToken(JwtBuilder builder) {
        return builder.signWith(key).compact();  // Key로 서명
    }

//    protected Key getKey() {
//        return this.key;
//    }
}