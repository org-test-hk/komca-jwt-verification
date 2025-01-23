package kr.or.komca.foundation.jwt.domain.models.auth;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 역할 도메인 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Role {
    /** 역할 번호 */
    private Long roleNo;

    /** 역할명 */
    private String roleName;

    /** 설명 */
    private String description;

    /** 등록일시 */
    private LocalDateTime regDate;

    /** 수정일시 */
    private LocalDateTime modDate;

    /** 삭제 여부 */
    private String delYn;

    @Builder.Default
    private List<Permission> permissions = new ArrayList<>();

    /**
     * Spring Security GrantedAuthority 변환
     */
    public GrantedAuthority toGrantedAuthority() {
        return new SimpleGrantedAuthority(this.roleName);
    }
}