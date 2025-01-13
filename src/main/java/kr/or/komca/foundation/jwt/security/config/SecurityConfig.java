package kr.or.komca.foundation.jwt.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.komca.foundation.jwt.logging.AuthenticationLogger;
import kr.or.komca.foundation.jwt.security.filter.JwtAuthenticationFilter;
import kr.or.komca.foundation.jwt.security.jwt.JwtTokenProvider;
import kr.or.komca.foundation.jwt.service.command.TokenCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

/**
 * Spring Security 설정
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "foundation.security", name = "standalone", havingValue = "true", matchIfMissing = false)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenCommandService tokenCommandService;
    private final AuthenticationLogger authenticationLogger;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(SecurityURLConstants.AUTH_URLS).permitAll()
                        .requestMatchers(SecurityURLConstants.SWAGGER_URLS).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider, tokenCommandService, authenticationLogger, Arrays.stream(SecurityURLConstants.PUBLIC_URLS).toList()),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
}