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

		configureJwtFilterPatterns(jwtAuthenticationFilter);

		http
				.csrf(AbstractHttpConfigurer::disable)
				.formLogin(AbstractHttpConfigurer::disable)
				.httpBasic(AbstractHttpConfigurer::disable)
				.sessionManagement(session ->
						session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				)
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

	protected void configureJwtFilterPatterns(JwtAuthenticationFilter jwtAuthenticationFilter) {
		jwtAuthenticationFilter.addSkipPatterns(SecurityURLConstants.PUBLIC_URLS);

		addJwtFilterSkipPatterns(jwtAuthenticationFilter);
	}

	/**
	 * 허용 URL 등록
	 * @param filter
	 */
	protected abstract void addJwtFilterSkipPatterns(JwtAuthenticationFilter filter);

	/**
	 * 권한 기반 필터링 (hasRole 등 사용)
	 * @param httpSecurity
	 */
	protected abstract void configureAuthorization(HttpSecurity httpSecurity) throws Exception;
}
