package kr.or.komca.foundation.jwt.exception.handler;

import kr.or.komca.foundation.jwt.exception.filterException.BaseAuthenticationException;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import kr.or.komca.komcadatacore.dto.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final AuthenticationLogger authLogger;

	@ExceptionHandler(BaseAuthenticationException.class)
	public CommonResponse<Void> handleAuthException(BaseAuthenticationException e) {
		authLogger.logAuthenticationError(e);
		return e.toResponse();
	}
}
