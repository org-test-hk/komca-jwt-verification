package kr.or.komca.foundation.jwt.global.exception.asis;

import org.springframework.http.HttpStatus;

/**
 * 만료된 토큰으로 인한 예외
 */
public class TokenExpiredException extends AuthException {
    public TokenExpiredException() {
        super(
                "Token has expired",
                "TOKEN_EXPIRED",
                HttpStatus.UNAUTHORIZED
        );
    }
}
