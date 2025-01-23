package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

public class MissingTokenException extends BaseAuthenticationException {
	public MissingTokenException() {
		super(AuthErrorCode.TOKEN_MISSING);
	}
}
