package kr.or.komca.foundation.jwt.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenAbnormalLog {
    private Long logId;
    private Long tokenId;
    private Long userNo;
    private String accessToken;
    private String eventType;
    private String severityLevel;
    private String details;
    private LocalDateTime occurredAt;
    private String ipAddress;
    private String userAgent;
    private String fingerprint;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String delYn;

    @Builder
    public TokenAbnormalLog(Long tokenId, Long userNo, String accessToken,
                            String eventType, String severityLevel, String details,
                            LocalDateTime occurredAt, String ipAddress,
                            String userAgent, String fingerprint) {
        this.tokenId = tokenId;
        this.userNo = userNo;
        this.accessToken = accessToken;
        this.eventType = eventType;
        this.severityLevel = severityLevel;
        this.details = details;
        this.occurredAt = occurredAt;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.fingerprint = fingerprint;
        this.delYn = "N";
    }
}
