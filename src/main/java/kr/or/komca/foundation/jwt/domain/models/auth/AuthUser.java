package kr.or.komca.foundation.jwt.domain.models.auth;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 도메인 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "password")
public class AuthUser {
    /** 사용자 번호 */
    private Long userNo;

    /** 사용자 아이디 */
    private String username;

    /** 비밀번호 (암호화됨) */
    private String password;

    /** 이름 */
    private String name;

    /** 이메일 */
    private String email;

    /** 계정 활성화 여부 */
    private boolean enabled;

    /** 역할 목록 */
    @Builder.Default
    private final List<Role> roles = new ArrayList<>();

    /** 등록일시 */
    private LocalDateTime regDate;

    /** 수정일시 */
    private LocalDateTime modDate;

    /** 삭제 여부 */
    private String delYn;

    /**
     * Spring Security UserDetails 변환
     */
    public UserDetails toUserDetails() {
        return new org.springframework.security.core.userdetails.User(
                this.username,
                this.password,
                this.enabled,
                true, // accountNonExpired
                true, // credentialsNonExpired
                true, // accountNonLocked
                this.roles.stream()
                        .map(Role::toGrantedAuthority)
                        .collect(Collectors.toList())
        );
    }
}