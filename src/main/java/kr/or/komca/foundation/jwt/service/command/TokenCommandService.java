package kr.or.komca.foundation.jwt.service.command;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.komca.foundation.jwt.domain.entity.AccessToken;
import kr.or.komca.foundation.jwt.dto.TokenValidationResult;

/**
 * 토큰 관련 명령 처리 서비스 인터페이스
 */
public interface TokenCommandService {
    /**
     * 새로운 액세스 토큰 저장
     * @param accessToken 저장할 액세스 토큰 정보
     */
    void saveAccessToken(AccessToken accessToken);

    /**
     * 토큰을 블랙리스트에 추가
     * @param token 블랙리스트에 추가할 토큰
     * @param reason 블랙리스트 추가 사유
     */
    void addToBlacklist(String token, String reason);

    /**
     * 비정상 접근 로그 기록
     * @param token 대상 토큰
     * @param eventType 이벤트 유형
     * @param request HTTP 요청 정보
     */
    void logAbnormalAccess(String token, String eventType, HttpServletRequest request);

    /**
     * 토큰의 유효성을 검증하고 필요한 보안 이벤트를 기록합니다.
     */
    TokenValidationResult validateToken(String token, HttpServletRequest request);
}