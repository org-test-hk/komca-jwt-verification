package kr.or.komca.foundation.jwt.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

@Slf4j
@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "로그인이 필요합니다");
	}

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
		sendErrorResponse(response, HttpStatus.FORBIDDEN, "권한이 부족합니다");
	}

	private void sendErrorResponse(HttpServletResponse response,
	                               HttpStatus status,
	                               String message) {
		try {
			response.setStatus(status.value());  // HTTP 상태 코드 설정
			response.setContentType("application/json;charset=UTF-8");  // JSON 응답 설정

			// 에러 응답 데이터 구성
			Map<String, Object> errorResponse = new HashMap<>();
			errorResponse.put("timestamp", new Date());  // 발생 시간
			errorResponse.put("status", status.value());  // 상태 코드
			errorResponse.put("error", status.getReasonPhrase());  // 에러 설명
			errorResponse.put("message", message);  // 에러 메시지

			// JSON 변환 후 응답
			String json = new ObjectMapper().writeValueAsString(errorResponse);
			response.getWriter().write(json);
		} catch (IOException ex) {
			log.error("Error sending security exception response", ex);  // 에러 발생시 로깅
		}
	}

}
