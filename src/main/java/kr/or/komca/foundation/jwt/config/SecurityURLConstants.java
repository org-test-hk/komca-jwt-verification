package kr.or.komca.foundation.jwt.config;

public class SecurityURLConstants {
    // 인증 관련 URL
    public static final String[] AUTH_URLS = {
            "/api/public/v1/auth/login",
            "/api/public/v1/auth/signup",
            "/api/public/v1/auth/refresh"
    };

    // Swagger 관련 URL
    public static final String[] SWAGGER_URLS = {
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    // 모든 Public URL
    public static final String[] PUBLIC_URLS = {
            "/api/public/v1/auth/login",
            "/api/public/v1/auth/signup",
            "/api/public/v1/auth/refresh",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };
}
