package kr.or.komca.foundation.jwt.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken {
    private Long tokenId;
    private Long userNo;
    private String accessToken;
    private String fingerprint;
    private String ipAddress;
    private String userAgent;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String devicePlatform;
    private String deviceResolution;
    private String deviceTimezone;
    private String delYn;

    @Builder
    public AccessToken(Long userNo, String accessToken, String fingerprint,
                       String ipAddress, String userAgent, String devicePlatform,
                       String deviceResolution, String deviceTimezone,
                       LocalDateTime issuedAt, LocalDateTime expiresAt) {
        this.userNo = userNo;
        this.accessToken = accessToken;
        this.fingerprint = fingerprint;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.devicePlatform = devicePlatform;
        this.deviceResolution = deviceResolution;
        this.deviceTimezone = deviceTimezone;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.delYn = "N";
    }
}
