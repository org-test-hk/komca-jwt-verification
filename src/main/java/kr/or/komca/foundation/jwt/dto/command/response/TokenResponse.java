package kr.or.komca.foundation.jwt.dto.command.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 토큰 응답 DTO
 */
@Getter
@Builder
@ToString
public class TokenResponse {
    /** 액세스 토큰 */
    private final String accessToken;

    /** 리프레시 토큰 */
    private final String refreshToken;

    /** 토큰 타입 */
    private final String tokenType;

    /** 만료 시간 (초) */
    private final long expiresIn;

    /** 사용자 정보 */
    private final UserResponse user;
}
