package kr.or.komca.foundation.jwt.security.config;

import kr.or.komca.foundation.jwt.common.ip.util.ClientIpUtil;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import kr.or.komca.foundation.jwt.mapper.command.TokenCommandMapper;
import kr.or.komca.foundation.jwt.mapper.query.TokenQueryMapper;
import kr.or.komca.foundation.jwt.mapper.query.UserQueryMapper;
import kr.or.komca.foundation.jwt.security.filter.JwtAuthenticationFilter;
import kr.or.komca.foundation.jwt.security.jwt.JwtProperties;
import kr.or.komca.foundation.jwt.security.jwt.JwtTokenProvider;
import kr.or.komca.foundation.jwt.service.command.TokenCommandService;
import kr.or.komca.foundation.jwt.service.command.TokenCommandServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;


@Configuration
@ComponentScan(basePackages = "kr.or.komca.foundation.jwt")
@RequiredArgsConstructor
public class JwtSecurityAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public JwtTokenProvider jwtTokenProvider(
			JwtProperties jwtProperties,
			UserQueryMapper userQueryMapper,
			TokenCommandService tokenCommandService) {
		return new JwtTokenProvider(
				jwtProperties,
				userQueryMapper,
				tokenCommandService
		);
	}

	@Bean
	@ConditionalOnMissingBean
	public ClientIpUtil clientIpUtil() {
		return new ClientIpUtil();
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
				authenticationLogger,
				Arrays.stream(SecurityURLConstants.PUBLIC_URLS).toList()
		);
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtProperties jwtProperties() {
		return new JwtProperties();
	}
}