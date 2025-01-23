package kr.or.komca.foundation.jwt.global.logging;

import jakarta.servlet.http.HttpServletRequest;

import kr.or.komca.foundation.jwt.domain.models.token.TokenValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AuthenticationLogger {
	public void logMissingToken(HttpServletRequest request) {
		log.debug("No JWT token found in request headers");
	}

	public void logMissingFingerprint(HttpServletRequest request, String jwt) {
		log.warn("Missing fingerprint in request with token: {}", jwt);
	}

	public void logInvalidToken(HttpServletRequest request, String jwt) {
		log.error("Invalid token or signature for request URI: {}", request.getRequestURI());
	}

	public void logValidationFailure(HttpServletRequest request, String jwt, TokenValidationResult validationResult) {
		log.error("Token validation failed: {}, URI: {}",
				validationResult.getErrorMessage(),
				request.getRequestURI()
		);
	}

	public void logValidationWarnings(String jwt, List<String> warnings) {
		warnings.forEach(warning ->
				log.warn("Token validation warning for token {}: {}", jwt, warning)
		);
	}

	public void logSuccessfulAuthentication(HttpServletRequest request, Authentication authentication) {
		log.debug("Set Authentication to security context for '{}', uri: {}",
				authentication.getName(),
				request.getRequestURI()
		);
	}

	public void logRequestDetails(HttpServletRequest request, String token) {
		if (log.isDebugEnabled()) {
			log.debug("Processing request - URI: {}, Method: {}, Token: {}, IP: {}, User-Agent: {}",
					request.getRequestURI(),
					request.getMethod(),
					token != null ? "[PRESENT]" : "[NONE]",
					request.getRemoteAddr(),
					request.getHeader("User-Agent")
			);
		}
	}

	public void logAuthenticationError(Exception e) {
		log.error("Cannot set user authentication: {}", e.getMessage());
	}
}
