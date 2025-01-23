package kr.or.komca.foundation.jwt.service.command;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.komca.foundation.jwt.domain.models.token.AccessToken;
import kr.or.komca.foundation.jwt.domain.models.token.TokenAbnormalLog;
import kr.or.komca.foundation.jwt.domain.models.token.TokenValidationResult;
import kr.or.komca.foundation.jwt.global.jwt.constants.SecurityConstants;
import kr.or.komca.foundation.jwt.global.utils.ip.ClientIpUtil;
import kr.or.komca.foundation.jwt.mapper.command.TokenVerificationCommandMapper;
import kr.or.komca.foundation.jwt.mapper.query.TokenQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenVerificationCommandServiceImpl implements TokenVerificationCommandService {

    private final TokenVerificationCommandMapper tokenCommandMapper;
    private final TokenQueryMapper tokenQueryMapper;
    private final ClientIpUtil clientIpUtil;


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
