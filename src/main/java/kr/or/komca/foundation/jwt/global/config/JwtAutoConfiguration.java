package kr.or.komca.foundation.jwt.global.config;

import kr.or.komca.foundation.jwt.global.exception.handler.GlobalExceptionHandler;
import kr.or.komca.foundation.jwt.global.exception.handler.SecurityExceptionHandler;
import kr.or.komca.foundation.jwt.global.filter.JwtAuthenticationFilter;
import kr.or.komca.foundation.jwt.global.jwt.JwtProperties;
import kr.or.komca.foundation.jwt.global.logging.AuthenticationLogger;
import kr.or.komca.foundation.jwt.global.utils.ip.ClientIpUtil;
import kr.or.komca.foundation.jwt.mapper.command.TokenVerificationCommandMapper;
import kr.or.komca.foundation.jwt.mapper.query.TokenQueryMapper;
import kr.or.komca.foundation.jwt.mapper.query.UserQueryMapper;
import kr.or.komca.foundation.jwt.service.CustomUserDetailsService;
import kr.or.komca.foundation.jwt.service.JwtTokenValidator;
import kr.or.komca.foundation.jwt.service.command.TokenVerificationCommandService;
import kr.or.komca.foundation.jwt.service.command.TokenVerificationCommandServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@MapperScan(basePackages = "kr.or.komca.foundation.jwt.mapper")
@Configuration
@RequiredArgsConstructor
public class JwtAutoConfiguration {


	@Bean
	public CustomUserDetailsService customUserDetailsService(UserQueryMapper userQueryMapper) {
		return new CustomUserDetailsService(userQueryMapper);
	}

	@Bean
	@ConditionalOnMissingBean
	public ClientIpUtil clientIpUtil() {
		return new ClientIpUtil();
	}

	@Bean
	@ConditionalOnMissingBean
	public JwtProperties jwtProperties() {
		return new JwtProperties();
	}

	@Bean
	@ConditionalOnMissingBean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@ConditionalOnMissingBean
	public TokenVerificationCommandService tokenCommandService (
			TokenVerificationCommandMapper commandMapper,
			TokenQueryMapper tokenQueryMapper,
			ClientIpUtil clientIpUtil
	) {
		return new TokenVerificationCommandServiceImpl(commandMapper, tokenQueryMapper, clientIpUtil);
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
	public JwtTokenValidator jwtTokenValidator(TokenVerificationCommandService tokenCommandService, JwtProperties jwtProperties) {
		return new JwtTokenValidator(
				jwtProperties, tokenCommandService
		);
	}


	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter(
			JwtTokenValidator jwtTokenValidator,
			TokenVerificationCommandService tokenCommandService,
			AuthenticationLogger authenticationLogger) {
		return new JwtAuthenticationFilter(
				jwtTokenValidator,
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

	@Bean
	@ConditionalOnMissingBean
	public GlobalExceptionHandler globalExceptionHandler(AuthenticationLogger authenticationLogger) {
		return new GlobalExceptionHandler(authenticationLogger);
	}
}