package kr.or.komca.foundation.jwt.exception.filterException;

import kr.or.komca.foundation.jwt.exception.ErrorCode.AuthErrorCode;

public class TokenExpiredException extends BaseAuthenticationException {
	public TokenExpiredException() {
		super(AuthErrorCode.TOKEN_EXPIRED);
	}
}
