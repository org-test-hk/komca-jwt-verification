package kr.or.komca.foundation.jwt.exception.filterException;

import kr.or.komca.foundation.jwt.exception.ErrorCode.AuthErrorCode;

public class AuthenticationSystemException extends BaseAuthenticationException {
	public AuthenticationSystemException(Exception e) {
		super(AuthErrorCode.AUTHENTICATION_ERROR);
	}
}

