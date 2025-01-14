package kr.or.komca.foundation.jwt.mapper.query;

import kr.or.komca.komcadatacore.dto.auth.AccessToken;
import kr.or.komca.komcadatacore.dto.auth.TokenBlacklist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TokenQueryMapper {
    Optional<AccessToken> findByAccessToken(@Param("accessToken") String accessToken);
    Optional<TokenBlacklist> findBlacklistedToken(@Param("accessToken") String accessToken);
    List<AccessToken> findActiveTokensByUserNo(@Param("userNo") Long userNo);
}
