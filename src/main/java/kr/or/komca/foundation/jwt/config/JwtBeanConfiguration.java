package kr.or.komca.foundation.jwt.config;

import kr.or.komca.authcore.service.CustomUserDetailsService;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JwtBeanConfiguration {

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
	public AuthenticationLogger authenticationLogger() {
		return new AuthenticationLogger();
	}

}
