package kr.or.komca.foundation.jwt.mapper.command;

import kr.or.komca.authcore.models.TokenAbnormalLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenVerificationCommandMapper {
    int insertTokenAbnormalLog(TokenAbnormalLog log);
}
