package kr.or.komca.foundation.jwt.exception.handler;

import kr.or.komca.foundation.jwt.common.response.CommonResponseFoundation;
import kr.or.komca.foundation.jwt.exception.filterException.BaseAuthenticationException;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
	private final AuthenticationLogger authLogger;

	@ExceptionHandler(BaseAuthenticationException.class)
	public CommonResponseFoundation<Void> handleAuthException(BaseAuthenticationException e) {
		authLogger.logAuthenticationError(e);
		return e.toResponse();
	}
}
