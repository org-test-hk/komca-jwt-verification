package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

import java.util.Map;

//public class AuthenticationSystemException extends BaseAuthenticationException {
//	public AuthenticationSystemException(Exception e) {
//		super(AuthErrorCode.AUTHENTICATION_ERROR);
//	}
//}
public class AuthenticationSystemException extends BaseAuthenticationException {
	public AuthenticationSystemException(Exception e) {
		super(AuthErrorCode.AUTHENTICATION_ERROR);
	}
}

