package kr.or.komca.foundation.jwt.global.exception.handler;

import kr.or.komca.foundation.jwt.domain.dto.response.FilterErrorResponse;
import kr.or.komca.foundation.jwt.global.exception.filterException.BaseAuthenticationException;
import kr.or.komca.foundation.jwt.global.logging.AuthenticationLogger;
import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final AuthenticationLogger authLogger;

	@ExceptionHandler(BaseAuthenticationException.class)
	public ResponseEntity<BaseResponse> handleAuthException(BaseAuthenticationException e) {
		authLogger.logAuthenticationError(e);

		FilterErrorResponse.ErrorDetail errorDetail = FilterErrorResponse.ErrorDetail.builder()
				.code(e.getErrorCode().getCode())
				.build();

		return FilterErrorResponse.of(e.getErrorCode(), List.of(errorDetail));
	}

	// SecurityConfig에서 발생한 에러 핸들링 추가 필요
//	@ExceptionHandler(BaseAuthenticationException.class)
//	public ResponseEntity<BaseResponse> handleAuthException(BaseAuthenticationException e) {
//		authLogger.logAuthenticationError(e);
//
//		FilterErrorResponse.ErrorDetail errorDetail = FilterErrorResponse.ErrorDetail.builder()
//				.code(e.getErrorCode().getCode())
//				.build();
//
//		return FilterErrorResponse.of(e.getErrorCode(), List.of(errorDetail));
//	}
}
