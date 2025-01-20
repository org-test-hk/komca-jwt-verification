package kr.or.komca.foundation.jwt.exception.asis;

import org.springframework.http.HttpStatus;

/**
 * 역할을 찾을 수 없을 때 발생하는 예외
 */
public class RoleNotFoundException extends AuthException {
    public RoleNotFoundException(String roleName) {
        super(
                String.format("Role not found: %s", roleName),
                "ROLE_NOT_FOUND",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
