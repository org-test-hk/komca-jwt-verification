package kr.or.komca.foundation.jwt.service.command;

import kr.or.komca.foundation.jwt.domain.entity.Permission;

/**
 * 권한 관련 명령을 처리하는 서비스 인터페이스
 * CQS 패턴의 Command 부분을 담당
 */
public interface PermissionCommandService {
    /**
     * 역할에 권한을 추가
     * @param roleNo 역할 번호
     * @param permissionNo 권한 번호
     */
    void addPermissionToRole(Long roleNo, Long permissionNo);

    /**
     * 역할에서 권한을 제거
     * @param roleNo 역할 번호
     * @param permissionNo 권한 번호
     */
    void removePermissionFromRole(Long roleNo, Long permissionNo);

    /**
     * 새로운 권한을 생성
     * @param permission 생성할 권한 정보
     * @return 생성된 권한 정보
     */
    Permission createPermission(Permission permission);
}