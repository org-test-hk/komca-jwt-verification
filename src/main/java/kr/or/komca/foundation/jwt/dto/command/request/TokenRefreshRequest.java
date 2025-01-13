package kr.or.komca.foundation.jwt.dto.command.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 토큰 갱신 요청 DTO
 */
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {
    /** 리프레시 토큰 */
    @NotBlank(message = "REFRESH_TOKEN_REQUIRED")
    private String refreshToken;
}
