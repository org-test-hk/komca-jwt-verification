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
	public ResponseEntity<BaseResponse<Void>> handleAuthException(BaseAuthenticationException e) {
		authLogger.logAuthenticationError(e);

		BaseResponse.ErrorDetail errorDetail = BaseResponse.ErrorDetail.builder()
				.code(e.getErrorCode().getCode())
				.params(e.getParams())  // params가 있으면 포함
				.build();

		return FilterErrorResponse.of(e.getErrorCode(), List.of(errorDetail));
	}
}
