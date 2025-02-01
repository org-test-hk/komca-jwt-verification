package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

/**
 *  유효하지 않은 토큰
 */
public class InvalidTokenException extends BaseAuthenticationException {
	public InvalidTokenException() {
		super(AuthErrorCode.TOKEN_INVALID);
	}
}