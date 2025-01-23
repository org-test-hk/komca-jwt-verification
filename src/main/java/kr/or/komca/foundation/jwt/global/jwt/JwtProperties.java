package kr.or.komca.foundation.jwt.global.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 설정 프로퍼티
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /** 토큰 시크릿 키 */
    private String secret;

    /** 액세스 토큰 만료 시간 (초) */
    private long accessTokenValidity;

    /** 리프레시 토큰 만료 시간 (초) */
    private long refreshTokenValidity;
}
