package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

public class TokenValidationException extends BaseAuthenticationException {
	public TokenValidationException(String customMessage) {
		super(AuthErrorCode.TOKEN_VALIDATION_FAILED);
	}
}
