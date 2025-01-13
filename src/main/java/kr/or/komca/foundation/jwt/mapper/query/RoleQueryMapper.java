package kr.or.komca.foundation.jwt.mapper.query;

import kr.or.komca.foundation.jwt.domain.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleQueryMapper {
    /**
     * 모든 역할 조회
     * @return 역할 목록
     */
    List<Role> findAllRoles();
}