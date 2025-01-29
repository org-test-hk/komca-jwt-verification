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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.convert.ApplicationConversionService.configure;

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

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		sendErrorResponse(response, AuthErrorCode.LOGIN_REQUIRED);
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		sendErrorResponse(response, AuthErrorCode.ACCESS_DENIED);
	}

//	private void sendErrorResponse(HttpServletResponse response,
//	                               HttpStatus status,
//	                               String message) {
//		try {
//			response.setStatus(status.value());  // HTTP 상태 코드 설정
//			response.setContentType("application/json;charset=UTF-8");  // JSON 응답 설정
//
//			// 에러 응답 데이터 구성
//			Map<String, Object> errorResponse = new HashMap<>();
//			errorResponse.put("timestamp", new Date());  // 발생 시간
//			errorResponse.put("status", status.value());  // 상태 코드
//			errorResponse.put("error", status.getReasonPhrase());  // 에러 설명
//			errorResponse.put("message", message);  // 에러 메시지
//
//			// JSON 변환 후 응답
//			String json = new ObjectMapper().writeValueAsString(errorResponse);
//			response.getWriter().write(json);
//		} catch (IOException ex) {
//			log.error("Error sending security exception response", ex);  // 에러 발생시 로깅
//		}
//	}
	private void sendErrorResponse(HttpServletResponse response, AuthErrorCode errorCode) throws IOException {
		response.setStatus(errorCode.getStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		// FilterErrorResponse를 직접 생성하고 ObjectMapper로 직렬화
		String json = objectMapper.writeValueAsString(FilterErrorResponse.of(errorCode).getBody());
		response.getWriter().write(json);
	}
}
