package kr.or.komca.foundation.jwt.exception.filterException;

import kr.or.komca.foundation.jwt.exception.ErrorCode.AuthErrorCode;

public class TokenValidationException extends BaseAuthenticationException {
	public TokenValidationException(String customMessage) {
		super(AuthErrorCode.TOKEN_VALIDATION_FAILED);
	}
}
