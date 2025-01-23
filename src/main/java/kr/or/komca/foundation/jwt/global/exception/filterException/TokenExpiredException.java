package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

public class TokenExpiredException extends BaseAuthenticationException {
	public TokenExpiredException() {
		super(AuthErrorCode.TOKEN_EXPIRED);
	}
}
