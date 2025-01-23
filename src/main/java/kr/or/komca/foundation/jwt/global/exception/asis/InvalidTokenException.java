package kr.or.komca.foundation.jwt.global.exception.asis;

import org.springframework.http.HttpStatus;

/**
 * 유효하지 않은 토큰으로 인한 예외
 */
public class InvalidTokenException extends AuthException {
    public InvalidTokenException(String message) {
        super(
                message,
                "INVALID_TOKEN",
                HttpStatus.UNAUTHORIZED
        );
    }
}