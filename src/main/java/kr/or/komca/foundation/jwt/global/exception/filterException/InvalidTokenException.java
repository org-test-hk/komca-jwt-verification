package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

public class InvalidTokenException extends BaseAuthenticationException {
	public InvalidTokenException() {
		super(AuthErrorCode.TOKEN_INVALID);
	}
}