package kr.or.komca.foundation.jwt.exception.asis;

import org.springframework.http.HttpStatus;

/**
 * 중복된 사용자가 존재할 때 발생하는 예외
 */
public class DuplicateUserException extends AuthException {
    public DuplicateUserException(String username) {
        super(
                String.format("Username already exists: %s", username),
                "DUPLICATE_USERNAME",
                HttpStatus.CONFLICT
        );
    }
}