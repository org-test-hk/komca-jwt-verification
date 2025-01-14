package kr.or.komca.foundation.jwt.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kr.or.komca.foundation.jwt.mapper.query.UserQueryMapper;
import kr.or.komca.foundation.jwt.security.constants.SecurityConstants;
import kr.or.komca.foundation.jwt.service.command.TokenCommandService;
import kr.or.komca.komcadatacore.dto.auth.AccessToken;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JWT 토큰 생성 및 검증을 담당하는 제공자 클래스
 * - 토큰 생성 (Access Token, Refresh Token)
 * - 토큰 검증
 * - 인증 정보 추출
 * - 토큰 저장 및 추적
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final UserQueryMapper userQueryMapperExternal;
    private Key key;
    private final TokenCommandService tokenCommandService;

    @PostConstruct
    protected void init() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        this.key = (SecretKey) Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Access Token 을 생성.
     * 생성된 토큰은 보안 추적을 위해 데이터베이스에도 저장.
     *
     * @param username 사용자 아이디
     * @param roles 사용자 권한 목록
     * @return 생성된 JWT Access Token
     */
    public String createAccessToken(String username, List<String> roles) {
        String token = Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtProperties.getAccessTokenValidity() * 1000))
                .signWith(key)
                .compact();

        saveAccessToken(username, token);  // 보안 추적을 위한 토큰 저장
        return token;
    }

    /**
     * 리프레시 토큰 생성
     */
    public String createRefreshToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + jwtProperties.getRefreshTokenValidity() * 1000))
                .signWith(key)
                .compact();
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
     * (비즈니스 규칙 검증은 TokenCommandService에서 수행)
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
     * 토큰 생성 공통 메소드
     */
    private String createToken(Claims claims, long validityInSeconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInSeconds * 1000);

        return Jwts.builder()
                .claims(claims)
                .issuedAt(now)
                .expiration(validity)
                .signWith(key)
                .compact();
    }

    /**
     * 토큰에서 Claims를 추출.
     *
     * @param token JWT 토큰
     * @return Claims 객체
     */
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey)key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public long getAccessTokenValidityInSeconds() {
        return jwtProperties.getAccessTokenValidity();
    }

    public long getRefreshTokenValidityInSeconds() {
        return jwtProperties.getRefreshTokenValidity();
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
     * Access Token 정보를 데이터베이스에 저장.
     * 보안 추적 및 토큰 관리를 위해 사용됩니다.
     *
     * @param username 사용자 아이디
     * @param token JWT 토큰
     */
    private void saveAccessToken(String username, String token) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();

        kr.or.komca.komcadatacore.dto.user.User user = userQueryMapperExternal.findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("User not found: " + username));

        AccessToken accessToken = AccessToken.builder()
                .userNo(user.getUserNo())
                .accessToken(token)
                .fingerprint(request.getHeader(SecurityConstants.HEADER_FINGERPRINT))
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader(SecurityConstants.HEADER_USER_AGENT))
                .devicePlatform(request.getHeader(SecurityConstants.HEADER_DEVICE_PLATFORM))
                .deviceResolution(request.getHeader(SecurityConstants.HEADER_DEVICE_RESOLUTION))
                .deviceTimezone(request.getHeader(SecurityConstants.HEADER_DEVICE_TIMEZONE))
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(jwtProperties.getAccessTokenValidity()))
                .build();

        tokenCommandService.saveAccessToken(accessToken);
    }

    /**
     * 클라이언트 디바이스 정보를 구성.
     *
     * @param request HTTP 요청
     * @return 디바이스 정보 JSON 문자열
     */
    private String buildDeviceInfo(HttpServletRequest request) {
        return String.format("""
                {
                    "platform": "%s",
                    "screenResolution": "%s",
                    "timezone": "%s"
                }""",
                request.getHeader(SecurityConstants.HEADER_DEVICE_PLATFORM),
                request.getHeader(SecurityConstants.HEADER_DEVICE_RESOLUTION),
                request.getHeader(SecurityConstants.HEADER_DEVICE_TIMEZONE));
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
}