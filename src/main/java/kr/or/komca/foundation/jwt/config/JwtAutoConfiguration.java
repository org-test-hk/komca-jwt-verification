package kr.or.komca.foundation.jwt.config;

import kr.or.komca.authcore.jwt.JwtProperties;
import kr.or.komca.authcore.jwt.JwtTokenProvider;
import kr.or.komca.authcore.service.command.TokenCommandService;
import kr.or.komca.authcore.utils.ClientIpUtil;
import kr.or.komca.foundation.jwt.exception.handler.SecurityExceptionHandler;
import kr.or.komca.foundation.jwt.filter.JwtAuthenticationFilter;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class JwtAutoConfiguration {

//	@Bean
//	@ConditionalOnMissingBean
//	public PasswordEncoder passwordEncoder() {
//		return new BCryptPasswordEncoder();
//	}


	@Bean
	@ConditionalOnMissingBean
	public ClientIpUtil clientIpUtil() {
		return new ClientIpUtil();
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtAuthenticationFilter jwtAuthenticationFilter(
			JwtTokenProvider tokenProvider,
			TokenCommandService tokenCommandService,
			AuthenticationLogger authenticationLogger) {
		return new JwtAuthenticationFilter(
				tokenProvider,
				tokenCommandService,
				authenticationLogger
		);
	}

	@Bean
	@ConditionalOnMissingBean
	public SecurityExceptionHandler securityExceptionHandler() {
		return new SecurityExceptionHandler();
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationLogger authenticationLogger() {
		return new AuthenticationLogger();
	}
}