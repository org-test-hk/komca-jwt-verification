package kr.or.komca.foundation.jwt.exception.asis;

import org.springframework.http.HttpStatus;

/**
 * 사용자를 찾을 수 없을 때 발생하는 예외
 */
public class UserNotFoundException extends AuthException {
    public UserNotFoundException(String username) {
        super(
                String.format("User not found with username: %s", username),
                "USER_NOT_FOUND",
                HttpStatus.NOT_FOUND
        );
    }
}