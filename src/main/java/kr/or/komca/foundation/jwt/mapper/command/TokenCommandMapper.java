package kr.or.komca.foundation.jwt.mapper.command;

import kr.or.komca.komcadatacore.dto.auth.AccessToken;
import kr.or.komca.komcadatacore.dto.auth.TokenAbnormalLog;
import kr.or.komca.komcadatacore.dto.auth.TokenBlacklist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 토큰 관련 명령 수행 Mapper
 */
@Mapper
public interface TokenCommandMapper {
    int insertAccessToken(AccessToken accessToken);
    int insertTokenBlacklist(TokenBlacklist blacklist);
    int insertTokenAbnormalLog(TokenAbnormalLog log);
}