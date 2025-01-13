package kr.or.komca.foundation.jwt.service.query;

import kr.or.komca.foundation.jwt.domain.entity.Role;
import kr.or.komca.foundation.jwt.exception.RoleNotFoundException;

/**
 * 역할 조회 서비스 인터페이스
 */
public interface RoleQueryService {
    /**
     * 역할명으로 역할 조회
     * @param roleName 역할명
     * @return 역할 정보
     * @throws RoleNotFoundException 역할을 찾을 수 없는 경우
     */
    Role getRoleByName(String roleName);
}