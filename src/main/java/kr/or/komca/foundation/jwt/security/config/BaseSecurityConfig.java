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
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				);

		this.configurePermitAll(http);


		http
				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling(handler ->
						handler
								.accessDeniedHandler(securityExceptionHandler)
								.authenticationEntryPoint(securityExceptionHandler)
				);

		// 인증 방식과 필터 추가
		this.configureAuthentication(http);

		// 각 서비스의 권한 설정 적용
		this.configureAuthorization(http);

		return http.build();
	}

	/**
	 * 인증방식과 필터 추가
	 *
	 * @param http
	 * @throws Exception
	 */
	protected abstract void configureAuthentication(HttpSecurity http) throws Exception;

	/**
	 * 권한 설정 적용
	 *
	 * @param http
	 * @throws Exception
	 */
	protected abstract void configureAuthorization(HttpSecurity http) throws Exception;

	/**
	 * 허용할 URL 작성
	 * http.authorizeHttpRequests(auth -> auth
	 * 		.requestMatchers(허용할 URL 목록)
	 * );
	 * @param http
	 * @throws Exception
	 */
	protected abstract void configurePermitAll(HttpSecurity http) throws Exception;
}
