package kr.or.komca.foundation.jwt.exception.filterException;

import kr.or.komca.foundation.jwt.exception.ErrorCode.AuthErrorCode;

public class MissingTokenException extends BaseAuthenticationException {
	public MissingTokenException() {
		super(AuthErrorCode.TOKEN_MISSING);
	}
}
