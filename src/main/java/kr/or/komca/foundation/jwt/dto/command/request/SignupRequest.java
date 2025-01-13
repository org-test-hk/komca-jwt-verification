package kr.or.komca.foundation.jwt.dto.command.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import kr.or.komca.foundation.jwt.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 회원가입 요청 DTO
 */
@Getter
@Builder
@ToString
public class SignupRequest {
    /** 사용자 아이디 */
    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(min = 4, max = 20, message = "USERNAME_SIZE")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "USERNAME_PATTERN")
    private final String username;

    /** 비밀번호 */
    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(min = 8, max = 20, message = "PASSWORD_SIZE")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "PASSWORD_PATTERN")
    private final String password;

    /** 이름 */
    @NotBlank(message = "NAME_REQUIRED")
    @Size(max = 50, message = "NAME_SIZE")
    private final String name;

    /** 이메일 */
    @NotBlank(message = "EMAIL_REQUIRED")
    @Email(message = "EMAIL_INVALID")
    private final String email;

    /** 역할 목록 */
    private final List<String> roles;

    /** 권한 목록 */
    private final List<String> permissions;

    /**
     * User 엔티티로 변환
     */
    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(this.username)
                .password(encodedPassword)
                .name(this.name)
                .email(this.email)
                .enabled(true)
                .build();
    }
}