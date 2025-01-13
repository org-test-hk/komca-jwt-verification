package kr.or.komca.foundation.jwt.dto.command.response;

import kr.or.komca.foundation.jwt.domain.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 역할 정보 응답 DTO
 */
@Getter
@Builder
@ToString
public class RoleResponse {
    /** 역할 번호 */
    private final Long roleNo;

    /** 역할명 */
    private final String roleName;

    /** 설명 */
    private final String description;

    /** permission */
    private final List<PermissionResponse> permissions;

    /**
     * Role 엔티티로부터 DTO 생성
     */
    public static RoleResponse from(Role role) {
        return RoleResponse.builder()
                .roleNo(role.getRoleNo())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .permissions(role.getPermissions().stream()
                        .map(PermissionResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}