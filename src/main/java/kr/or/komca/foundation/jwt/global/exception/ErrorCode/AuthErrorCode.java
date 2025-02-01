package kr.or.komca.foundation.jwt.global.exception.ErrorCode;

import kr.or.komca.komcacommoninterface.response_code.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {
	// 토큰 검증 관련
	TOKEN_MISSING("TOKEN_MISSING", HttpStatus.UNAUTHORIZED),
	FINGERPRINT_MISSING("FINGERPRINT_MISSING", HttpStatus.UNAUTHORIZED),
	TOKEN_INVALID("TOKEN_INVALID", HttpStatus.UNAUTHORIZED),
	TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED),
	TOKEN_VALIDATION_FAILED("TOKEN_VALIDATION_FAILED", HttpStatus.UNAUTHORIZED),
	INVALID_REFRESH_TOKEN("INVALID_REFRESH_TOKEN", HttpStatus.UNAUTHORIZED),

	// 권한 관련
	ACCESS_DENIED("ACCESS_DENIED", HttpStatus.FORBIDDEN),  // 권한 부족
	LOGIN_REQUIRED("LOGIN_REQUIRED", HttpStatus.UNAUTHORIZED),  // 로그인 필요

	// 시스템 관련
	AUTHENTICATION_ERROR("AUTHENTICATION_ERROR", HttpStatus.UNAUTHORIZED);


	private final String code;
	private final HttpStatus status;

}
