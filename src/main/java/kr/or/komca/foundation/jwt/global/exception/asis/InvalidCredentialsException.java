package kr.or.komca.foundation.jwt.global.exception.asis;

import org.springframework.http.HttpStatus;

/**
 * 잘못된 인증 정보로 인한 예외
 */
public class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
        super(
                "Invalid username or password",
                "INVALID_CREDENTIALS",
                HttpStatus.UNAUTHORIZED
        );
    }
}