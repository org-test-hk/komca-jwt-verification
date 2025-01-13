package kr.or.komca.foundation.jwt.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenBlacklist {
    private Long blacklistId;
    private Long tokenId;
    private String accessToken;
    private String reason;
    private LocalDateTime blacklistedAt;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private String delYn;

    @Builder
    public TokenBlacklist(Long tokenId, String accessToken, String reason, LocalDateTime blacklistedAt) {
        this.tokenId = tokenId;
        this.accessToken = accessToken;
        this.reason = reason;
        this.blacklistedAt = blacklistedAt;
        this.delYn = "N";
    }
}
