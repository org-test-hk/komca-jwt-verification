package kr.or.komca.foundation.jwt.service.query;

import kr.or.komca.foundation.jwt.domain.entity.Permission;

import java.util.List;

/**
 * 권한 조회 서비스 인터페이스
 * CQS 패턴의 Query 부분을 담당
 */
public interface PermissionQueryService {
    /**
     * 특정 역할에 부여된 권한 목록을 조회
     * @param roleNo 역할 번호
     * @return 권한 목록
     */
    List<Permission> getPermissionsByRoleNo(Long roleNo);

    /**
     * 전체 권한 목록을 조회
     * @return 모든 권한 목록
     */
    List<Permission> getAllPermissions();
}