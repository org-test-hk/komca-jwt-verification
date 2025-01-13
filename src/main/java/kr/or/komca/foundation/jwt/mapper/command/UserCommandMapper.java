package kr.or.komca.foundation.jwt.mapper.command;

import kr.or.komca.foundation.jwt.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 사용자 관련 명령 수행 Mapper
 */
@Mapper
public interface UserCommandMapper {
    /**
     * 새로운 사용자 등록
     * @param user 등록할 사용자 정보
     * @return 영향받은 행의 수
     */
    int insertUser(User user);

    /**
     * 사용자 역할 등록
     * @param userNo 사용자 번호
     * @param roleNo 역할 번호
     * @return 영향받은 행의 수
     */
    int insertUserRole(@Param("userNo") Long userNo, @Param("roleNo") Long roleNo);

    /**
     * 리프레시 토큰 저장
     * @param userNo 사용자 번호
     * @param token 토큰
     * @param expiresAt 만료일시
     * @return 영향받은 행의 수
     */
    int insertRefreshToken(@Param("userNo") Long userNo,
                           @Param("token") String token,
                           @Param("expiresAt") LocalDateTime expiresAt);
}