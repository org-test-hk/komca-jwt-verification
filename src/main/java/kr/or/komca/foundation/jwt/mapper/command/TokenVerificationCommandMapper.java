package kr.or.komca.foundation.jwt.mapper.command;

import kr.or.komca.foundation.jwt.domain.models.token.TokenAbnormalLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TokenVerificationCommandMapper {
    int insertTokenAbnormalLog(TokenAbnormalLog log);
}
