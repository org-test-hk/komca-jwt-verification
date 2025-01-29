package kr.or.komca.foundation.jwt.global.exception.ErrorCode;

import kr.or.komca.komcacommoninterface.response_code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
//	TOKEN_MISSING("AUTH001", "No token found"),
//	FINGERPRINT_MISSING("AUTH002", "Missing fingerprint"),
//	TOKEN_INVALID("AUTH003", "Invalid token signature"),
//	TOKEN_EXPIRED("AUTH004", "Token has expired"),
//	TOKEN_VALIDATION_FAILED("AUTH005", "Token validation failed"),
//	AUTHENTICATION_ERROR("AUTH999", "Authentication processing error");
	TOKEN_MISSING("AUTH001", HttpStatus.UNAUTHORIZED),
	FINGERPRINT_MISSING("AUTH002", HttpStatus.UNAUTHORIZED),
	TOKEN_INVALID("AUTH003", HttpStatus.UNAUTHORIZED),
	TOKEN_EXPIRED("AUTH004", HttpStatus.UNAUTHORIZED),
	TOKEN_VALIDATION_FAILED("AUTH005", HttpStatus.UNAUTHORIZED),
	Role_NOT_FOUND("AUTH006", HttpStatus.UNAUTHORIZED),
	AUTHENTICATION_ERROR("AUTH999", HttpStatus.UNAUTHORIZED);

	private final String code;
	private final HttpStatus status;


}
