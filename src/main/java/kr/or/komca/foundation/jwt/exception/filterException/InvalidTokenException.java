package kr.or.komca.foundation.jwt.exception.filterException;

import kr.or.komca.foundation.jwt.exception.ErrorCode.AuthErrorCode;

public class InvalidTokenException extends BaseAuthenticationException {
	public InvalidTokenException() {
		super(AuthErrorCode.TOKEN_INVALID);
	}
}