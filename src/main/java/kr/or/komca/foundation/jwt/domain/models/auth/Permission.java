package kr.or.komca.foundation.jwt.domain.models.auth;

import lombok.*;

import java.time.LocalDateTime;

/**
 * 권한 정보를 나타내는 도메인 엔티티
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Permission {
    /** 권한 번호 */
    private Long permissionNo;

    /** 권한명 */
    private String permissionName;

    /** 권한 설명 */
    private String description;

    /** 등록일시 */
    private LocalDateTime regDate;

    /** 수정일시 */
    private LocalDateTime modDate;

    /** 삭제 여부 */
    private String delYn;
}