package kr.or.komca.foundation.jwt.config;

import kr.or.komca.authcore.jwt.JwtProperties;
import kr.or.komca.authcore.service.command.TokenCommandService;
import kr.or.komca.foundation.jwt.exception.handler.SecurityExceptionHandler;
import kr.or.komca.foundation.jwt.filter.JwtAuthenticationFilter;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import kr.or.komca.foundation.jwt.mapper.query.UserQueryMapper;
import kr.or.komca.foundation.jwt.service.CustomUserDetailsService;
import kr.or.komca.foundation.jwt.service.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@MapperScan(basePackages = "kr.or.komca.foundation.jwt.mapper")
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
	public AuthenticationManager authenticationManager (
			CustomUserDetailsService userDetailsService,  // UserDetailsService 구현체
			PasswordEncoder passwordEncoder) {

		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(userDetailsService);
		provider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(provider);
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtTokenProvider jwtTokenProvider(UserQueryMapper userQueryMapper, TokenCommandService tokenCommandService) {
		return new JwtTokenProvider(
				new JwtProperties(), userQueryMapper, tokenCommandService
		);
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