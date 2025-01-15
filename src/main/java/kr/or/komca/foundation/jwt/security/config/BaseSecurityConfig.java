package kr.or.komca.foundation.jwt.security.config;

import kr.or.komca.foundation.jwt.exception.handler.SecurityExceptionHandler;
import kr.or.komca.foundation.jwt.security.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Common Security Module의 추상 클래스
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public abstract class BaseSecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final SecurityExceptionHandler securityExceptionHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(handler ->
						handler.accessDeniedHandler(securityExceptionHandler)
								.authenticationEntryPoint(securityExceptionHandler));

		// 각 서비스의 권한 설정 적용
		configureAuthorization(http);

		return http.build();
	}

	// HttpSecurity를 직접 받아서 설정
	protected abstract void configureAuthorization(HttpSecurity http) throws Exception;
}
