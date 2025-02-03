package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

import java.util.Map;

//public class TokenValidationException extends BaseAuthenticationException {
//	public TokenValidationException(String customMessage) {
//		super(AuthErrorCode.TOKEN_VALIDATION_FAILED);
//	}
//}
public class TokenValidationException extends BaseAuthenticationException {
	public TokenValidationException(Map<String, Object> details) {
		super(AuthErrorCode.TOKEN_VALIDATION_FAILED);
	}
}
