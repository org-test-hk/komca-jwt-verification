package kr.or.komca.foundation.jwt.dto.command.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 로그인 요청 DTO
 */
@Getter
@Builder
@ToString(exclude = "password")
public class LoginRequest {
    /** 사용자 아이디 */
    @NotBlank(message = "USERNAME_REQUIRED")
    private final String username;

    /** 비밀번호 */
    @NotBlank(message = "PASSWORD_REQUIRED")
    private final String password;
}