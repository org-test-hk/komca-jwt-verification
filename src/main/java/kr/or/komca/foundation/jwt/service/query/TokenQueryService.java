package kr.or.komca.foundation.jwt.service.query;

import kr.or.komca.foundation.jwt.domain.entity.AccessToken;

import java.util.Optional;

/**
 * 토큰 관련 조회 서비스 인터페이스
 */
public interface TokenQueryService {
    /**
     * 액세스 토큰으로 토큰 정보 조회
     * @param token 조회할 액세스 토큰
     * @return 토큰 정보
     */
    Optional<AccessToken> findByAccessToken(String token);

    /**
     * 블랙리스트 토큰 확인
     * @param token 확인할 토큰
     * @return 블랙리스트 포함 여부
     */
    boolean isTokenBlacklisted(String token);

}