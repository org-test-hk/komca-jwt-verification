package kr.or.komca.foundation.jwt.mapper.query;

import kr.or.komca.foundation.jwt.domain.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 권한 조회 관련 MyBatis 매퍼 인터페이스
 */
@Mapper
public interface PermissionQueryMapper {
    /**
     * 역할 번호로 해당 역할에 부여된 권한 목록을 조회
     * @param roleNo 역할 번호
     * @return 권한 목록
     */
    List<Permission> findByRoleNo(@Param("roleNo") Long roleNo);

    /**
     * 권한명으로 권한 정보를 조회
     * @param permissionName 권한명
     * @return 권한 정보 (Optional)
     */
    Optional<Permission> findByPermissionName(@Param("permissionName") String permissionName);

    /**
     * 모든 권한 목록을 조회
     * @return 전체 권한 목록
     */
    List<Permission> findAll();
}