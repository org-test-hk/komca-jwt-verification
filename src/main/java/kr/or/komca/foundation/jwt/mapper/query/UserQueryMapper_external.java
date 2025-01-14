package kr.or.komca.foundation.jwt.mapper.query;

import kr.or.komca.foundation.jwt.domain.entity.Role;
import kr.or.komca.foundation.jwt.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 관련 조회 수행 Mapper
 */
@Mapper
public interface UserQueryMapper_external {
    /**
     * 사용자 정보 조회 (역할 포함)
     * @param username 사용자 아이디
     * @return 사용자 정보
     */
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * 사용자의 역할 목록 조회
     * @param userNo 사용자 번호
     * @return 역할 목록
     */
    List<Role> selectUserRoles(@Param("userNo") Long userNo);

    /**
     * 리프레시 토큰으로 사용자 조회
     * @param token 리프레시 토큰
     * @return 사용자 정보
     */
    Optional<User> findByRefreshToken(@Param("token") String token);
}
