package kr.or.komca.foundation.jwt.global.exception.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.komca.foundation.jwt.domain.dto.response.FilterErrorResponse;
import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;
import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

	private final ObjectMapper objectMapper;

	public SecurityExceptionHandler() {
		this.objectMapper = new ObjectMapper()
				.registerModule(new JavaTimeModule())  // LocalDateTime 직렬화를 위한 모듈 추가
				.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)  // 타임스탬프 형식이 아닌 ISO-8601 형식으로 직렬화
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);
	}

	/** 인증되지 않은 사용자의 요청 처리 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		sendErrorResponse(response, AuthErrorCode.LOGIN_REQUIRED);
	}


	/** 접근 권한이 없는 요청 처리 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		sendErrorResponse(response, AuthErrorCode.ACCESS_DENIED);
	}

	private void sendErrorResponse(HttpServletResponse response, AuthErrorCode errorCode) throws IOException {

		log.error("핸들러 에러");

		response.setStatus(errorCode.getStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		// FilterErrorResponse의 정적 팩토리 메서드 사용
		FilterErrorResponse.ErrorDetail errorDetail = FilterErrorResponse.ErrorDetail.builder()
				.code(errorCode.getCode())
				.build();

		BaseResponse errorResponse = FilterErrorResponse.of(
				errorCode,
				List.of(errorDetail)
		).getBody();

		String json = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(json);
	}
}
