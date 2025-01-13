package kr.or.komca.foundation.jwt.service.query;

import kr.or.komca.foundation.jwt.domain.entity.AccessToken;
import kr.or.komca.foundation.jwt.mapper.query.TokenQueryMapper;
import kr.or.komca.foundation.jwt.service.command.TokenCommandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 토큰 조회 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenQueryServiceImpl implements TokenQueryService {

    private final TokenQueryMapper tokenQueryMapper;
    private final TokenCommandService tokenCommandService;

    @Override
    public Optional<AccessToken> findByAccessToken(String token) {
        return tokenQueryMapper.findByAccessToken(token);
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        return tokenQueryMapper.findBlacklistedToken(token).isPresent();
    }
}