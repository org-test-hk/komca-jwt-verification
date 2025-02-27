package kr.or.komca.foundation.jwt.global.config;

import kr.or.komca.foundation.jwt.global.logging.AuthenticationLogger;
import kr.or.komca.foundation.jwt.service.CustomUserDetailsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class JwtBeanConfiguration {


}
