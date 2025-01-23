package kr.or.komca.foundation.jwt.global.exception.filterException;

import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;

public class MissingFingerprintException extends BaseAuthenticationException {
	public MissingFingerprintException() {
		super(AuthErrorCode.FINGERPRINT_MISSING);
	}
}