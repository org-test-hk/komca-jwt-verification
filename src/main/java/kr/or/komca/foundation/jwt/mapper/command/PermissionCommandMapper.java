package kr.or.komca.foundation.jwt.mapper.command;

import kr.or.komca.foundation.jwt.domain.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 권한 관련 명령을 처리하는 MyBatis 매퍼 인터페이스
 */
@Mapper
public interface PermissionCommandMapper {
    /**
     * 새로운 권한을 등록
     * @param permission 등록할 권한 정보
     * @return 영향받은 행의 수
     */
    int insertPermission(Permission permission);

    /**
     * 역할에 권한을 부여
     * @param roleNo 역할 번호
     * @param permissionNo 권한 번호
     * @return 영향받은 행의 수
     */
    int insertRolePermission(@Param("roleNo") Long roleNo, @Param("permissionNo") Long permissionNo);

    /**
     * 역할에서 권한을 제거 (논리적 삭제)
     * @param roleNo 역할 번호
     * @param permissionNo 권한 번호
     * @return 영향받은 행의 수
     */
    int deleteRolePermission(@Param("roleNo") Long roleNo, @Param("permissionNo") Long permissionNo);
}