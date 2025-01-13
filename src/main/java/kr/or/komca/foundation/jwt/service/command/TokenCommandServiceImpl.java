package kr.or.komca.foundation.jwt.service.command;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.komca.foundation.jwt.common.ip.util.ClientIpUtil;
import kr.or.komca.foundation.jwt.domain.entity.AccessToken;
import kr.or.komca.foundation.jwt.domain.entity.TokenAbnormalLog;
import kr.or.komca.foundation.jwt.domain.entity.TokenBlacklist;
import kr.or.komca.foundation.jwt.dto.TokenValidationResult;
import kr.or.komca.foundation.jwt.mapper.command.TokenCommandMapper;
import kr.or.komca.foundation.jwt.mapper.query.TokenQueryMapper;
import kr.or.komca.foundation.jwt.security.constants.SecurityConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 토큰 명령 처리 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TokenCommandServiceImpl implements TokenCommandService {

    private final TokenCommandMapper tokenCommandMapper;
    private final TokenQueryMapper tokenQueryMapper;
    private final ClientIpUtil clientIpUtil;

    @Override
    @Transactional
    public void saveAccessToken(AccessToken accessToken) {
        log.debug("Saving access token for user: {}", accessToken.getUserNo());
        tokenCommandMapper.insertAccessToken(accessToken);
    }

    @Override
    @Transactional
    public void addToBlacklist(String token, String reason) {
        log.debug("Adding token to blacklist, reason: {}", reason);
        TokenBlacklist blacklist = TokenBlacklist.builder()
                .accessToken(token)
                .reason(reason)
                .blacklistedAt(LocalDateTime.now())
                .build();
        tokenCommandMapper.insertTokenBlacklist(blacklist);
    }

    @Override
    @Transactional
    public void logAbnormalAccess(String token, String eventType, HttpServletRequest request) {
        log.debug("Logging abnormal access event: {}", eventType);

        Long userNo = null;
        try {
            userNo = tokenQueryMapper.findByAccessToken(token)
                    .map(AccessToken::getUserNo)
                    .orElse(null);
        } catch (Exception e) {
            log.warn("Could not find userNo for token during abnormal access logging", e);
        }

        TokenAbnormalLog abnormalLog = TokenAbnormalLog.builder()
                .userNo(userNo)  // userNo가 없는 경우 null이 들어감
                .accessToken(token)
                .eventType(eventType)
                .severityLevel(getSeverityLevel(eventType))
                .occurredAt(LocalDateTime.now())
                .ipAddress(request.getRemoteAddr())
                .userAgent(request.getHeader(SecurityConstants.HEADER_USER_AGENT))
                .fingerprint(request.getHeader(SecurityConstants.HEADER_FINGERPRINT))
                .details(buildAbnormalAccessDetails(request))
                .build();

        tokenCommandMapper.insertTokenAbnormalLog(abnormalLog);
    }

    @Override
    @Transactional
    public TokenValidationResult validateToken(String token, HttpServletRequest request) {
        // 1. 블랙리스트 체크
        if (tokenQueryMapper.findBlacklistedToken(token).isPresent()) {
            logAbnormalAccess(token, SecurityConstants.EVENT_BLACKLISTED, request);
            return TokenValidationResult.invalid("Token is blacklisted");
        }

        // 2. 토큰 존재 여부 확인
        Optional<AccessToken> savedToken = tokenQueryMapper.findByAccessToken(token);
        if (savedToken.isEmpty()) {
            return TokenValidationResult.invalid("Token not found");
        }

        return validateClientInfo(savedToken.get(), request);
    }

    private TokenValidationResult validateClientInfo(AccessToken savedToken, HttpServletRequest request) {
        String currentFingerprint = request.getHeader(SecurityConstants.HEADER_FINGERPRINT);
        String currentIp = clientIpUtil.getClientIp(request);
        String userAgent = request.getHeader(SecurityConstants.HEADER_USER_AGENT);

        // Fingerprint 검증
        if (!savedToken.getFingerprint().equals(currentFingerprint)) {
            log.warn("Fingerprint mismatch detected - saved: {}, current: {}",
                    savedToken.getFingerprint(), currentFingerprint);
            logAbnormalAccess(savedToken.getAccessToken(),
                    SecurityConstants.EVENT_CLIENT_INFO_MISMATCH, request);
            return TokenValidationResult.invalid("Fingerprint mismatch");
        }

        // IP 변경 검증
        if (clientIpUtil.isSuspiciousIpChange(
                savedToken.getIpAddress(),
                currentIp,
                userAgent,
                savedToken.getDevicePlatform())) {
            log.warn("Suspicious IP change detected - original: {}, current: {}",
                    savedToken.getIpAddress(), currentIp);
            logAbnormalAccess(savedToken.getAccessToken(),
                    SecurityConstants.EVENT_IP_CHANGED, request);
            return TokenValidationResult.withWarning(
                    String.format("Suspicious IP address change detected from %s to %s",
                            savedToken.getIpAddress(), currentIp));
        }

        return TokenValidationResult.valid();
    }

    private String getSeverityLevel(String eventType) {
        return switch (eventType) {
            case SecurityConstants.EVENT_INVALID_SIGNATURE,
                 SecurityConstants.EVENT_MULTIPLE_TOKENS -> SecurityConstants.SEVERITY_HIGH;
            case SecurityConstants.EVENT_IP_CHANGED,
                 SecurityConstants.EVENT_CLIENT_INFO_MISMATCH -> SecurityConstants.SEVERITY_MEDIUM;
            default -> SecurityConstants.SEVERITY_LOW;
        };
    }

    private String buildAbnormalAccessDetails(HttpServletRequest request) {
        return String.format("""
                {
                    "requestUri": "%s",
                    "method": "%s",
                    "referer": "%s",
                    "origin": "%s"
                }""",
                request.getRequestURI(),
                request.getMethod(),
                request.getHeader("Referer"),
                request.getHeader("Origin"));
    }
}