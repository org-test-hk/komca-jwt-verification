package kr.or.komca.foundation.jwt.exception.ErrorCode;

import lombok.Getter;

@Getter
public enum AuthErrorCode {
	TOKEN_MISSING("AUTH001", "No token found"),
	FINGERPRINT_MISSING("AUTH002", "Missing fingerprint"),
	TOKEN_INVALID("AUTH003", "Invalid token signature"),
	TOKEN_EXPIRED("AUTH004", "Token has expired"),
	TOKEN_VALIDATION_FAILED("AUTH005", "Token validation failed"),
	AUTHENTICATION_ERROR("AUTH999", "Authentication processing error");

	private final String code;
	private final String message;

	AuthErrorCode(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
