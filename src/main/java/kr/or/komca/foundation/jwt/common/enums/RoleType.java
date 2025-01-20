package kr.or.komca.foundation.jwt.common.enums;

import lombok.Getter;

import java.util.Arrays;

/**
 * 시스템에서 사용되는 역할 타입 정의
 */
@Getter
public enum RoleType {
    ADMIN("ROLE_ADMIN", "시스템 관리자"),
    MANAGER("ROLE_MANAGER", "운영 관리자"),
    USER("ROLE_USER", "일반 사용자");

    private final String roleName;
    private final String description;

    RoleType(String roleName, String description) {
        this.roleName = roleName;
        this.description = description;
    }

    public static RoleType fromRoleName(String roleName) {
        return Arrays.stream(values())
                .filter(role -> role.getRoleName().equals(roleName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role name: " + roleName));
    }
}