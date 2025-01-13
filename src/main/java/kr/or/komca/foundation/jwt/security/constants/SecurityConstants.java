package kr.or.komca.foundation.jwt.security.constants;

/**
 * 보안 관련 상수 정의
 */
public class SecurityConstants {
    // Token Event Types
    public static final String EVENT_INVALID_SIGNATURE = "INVALID_SIGNATURE";
    public static final String EVENT_TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String EVENT_MULTIPLE_TOKENS = "MULTIPLE_TOKENS";
    public static final String EVENT_IP_CHANGED = "IP_CHANGED";
    public static final String EVENT_CLIENT_INFO_MISMATCH = "CLIENT_INFO_MISMATCH";
    public static final String EVENT_BLACKLISTED = "BLACKLISTED";
    public static final String EVENT_MISSING_FINGERPRINT = "MISSING_FINGERPRINT";
    public static final String EVENT_UNSUPPORTED_TOKEN = "UNSUPPORTED_TOKEN";
    public static final String EVENT_INVALID_TOKEN = "INVALID_TOKEN";

    // Severity Levels
    public static final String SEVERITY_HIGH = "HIGH";
    public static final String SEVERITY_MEDIUM = "MEDIUM";
    public static final String SEVERITY_LOW = "LOW";

    // HTTP Headers
    public static final String HEADER_FINGERPRINT = "X-Fingerprint";
    public static final String HEADER_FORWARDED_FOR = "X-Forwarded-For";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_DEVICE_PLATFORM = "X-Device-Platform";
    public static final String HEADER_DEVICE_RESOLUTION = "X-Device-Resolution";
    public static final String HEADER_DEVICE_TIMEZONE = "X-Device-Timezone";

    // Token Types
    public static final String TOKEN_TYPE_BEARER = "Bearer ";

    // Error Messages
    public static final String ERROR_TOKEN_BLACKLISTED = "Token is blacklisted";
    public static final String ERROR_TOKEN_NOT_FOUND = "Token not found in database";
    public static final String ERROR_INVALID_TOKEN = "Invalid token";
}