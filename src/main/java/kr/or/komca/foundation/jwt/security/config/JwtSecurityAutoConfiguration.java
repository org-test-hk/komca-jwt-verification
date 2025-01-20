package kr.or.komca.foundation.jwt.security.config;

import kr.or.komca.authcore.jwt.JwtProperties;
import kr.or.komca.authcore.jwt.JwtTokenProvider;
import kr.or.komca.authcore.mapper.command.TokenCommandMapper;
import kr.or.komca.authcore.mapper.query.TokenQueryMapper;
import kr.or.komca.authcore.mapper.query.UserQueryMapper;
import kr.or.komca.authcore.service.command.TokenCommandService;
import kr.or.komca.authcore.service.command.TokenCommandServiceImpl;
import kr.or.komca.authcore.utils.ClientIpUtil;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import kr.or.komca.foundation.jwt.security.filter.JwtAuthenticationFilter;
import kr.or.komca.foundation.jwt.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@ComponentScan(basePackages = "kr.or.komca.foundation.jwt")
@RequiredArgsConstructor
public class JwtSecurityAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

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
	public JwtTokenProvider jwtTokenProvider(
			JwtProperties jwtProperties,
			UserQueryMapper userQueryMapperExternal,
			TokenCommandService tokenCommandService) {
		return new JwtTokenProvider(
				jwtProperties,
				userQueryMapperExternal,
				tokenCommandService
		);
	}

	@Bean
	@ConditionalOnMissingBean
	public TokenCommandService tokenCommandService(TokenCommandMapper tokenCommandMapper, TokenQueryMapper tokenQueryMapper, ClientIpUtil clientIpUtil) {
		return new TokenCommandServiceImpl(tokenCommandMapper, tokenQueryMapper, clientIpUtil);
	}

	@Bean
	@ConditionalOnMissingBean
	public AuthenticationLogger authenticationLogger() {
		return new AuthenticationLogger();
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
	public JwtProperties jwtProperties() {
		return new JwtProperties();
	}
}