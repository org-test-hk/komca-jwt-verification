package kr.or.komca.foundation.jwt.exception.filterException;

import kr.or.komca.foundation.jwt.exception.ErrorCode.AuthErrorCode;

public class MissingFingerprintException extends BaseAuthenticationException {
	public MissingFingerprintException() {
		super(AuthErrorCode.FINGERPRINT_MISSING);
	}
}