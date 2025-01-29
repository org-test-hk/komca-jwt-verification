package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;
import kr.or.komca.foundation.jwt.global.exception.asis.AuthException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;

/**
 * 역할을 찾을 수 없을 때 발생하는 예외
 */
@Log4j2
public class RoleNotFoundException extends BaseAuthenticationException {
    public RoleNotFoundException() {
        super(AuthErrorCode.Role_NOT_FOUND);
    }

    public RoleNotFoundException(String roleName) {
        super(AuthErrorCode.Role_NOT_FOUND);
        log.error("Role not found: {}", roleName);
    }
}
