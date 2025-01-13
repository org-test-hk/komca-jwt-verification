package kr.or.komca.foundation.jwt.dto.command.response;

import kr.or.komca.foundation.jwt.domain.entity.Permission;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * 권한 정보 응답 DTO
 * 클라이언트에 반환할 권한 정보를 담는 불변 객체
 */
@Getter
@Builder
@ToString
public class PermissionResponse {
    /** 권한 번호 */
    private final Long permissionNo;

    /** 권한명 */
    private final String permissionName;

    /** 권한 설명 */
    private final String description;

    /**
     * Permission 엔티티를 DTO로 변환
     * @param permission 변환할 Permission 엔티티
     * @return PermissionResponse DTO
     */
    public static PermissionResponse from(Permission permission) {
        return PermissionResponse.builder()
                .permissionNo(permission.getPermissionNo())
                .permissionName(permission.getPermissionName())
                .description(permission.getDescription())
                .build();
    }
}