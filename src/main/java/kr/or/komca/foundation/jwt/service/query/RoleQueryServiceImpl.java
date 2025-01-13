package kr.or.komca.foundation.jwt.service.query;

import jakarta.annotation.PostConstruct;
import kr.or.komca.foundation.jwt.domain.entity.Role;
import kr.or.komca.foundation.jwt.exception.RoleNotFoundException;
import kr.or.komca.foundation.jwt.mapper.query.RoleQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * 역할 조회 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleQueryServiceImpl implements RoleQueryService {
    private final RoleQueryMapper roleQueryMapper;
    private Map<String, Role> roleCache;

    @PostConstruct
    public void init() {
        log.info("Initializing role cache");
        roleCache = roleQueryMapper.findAllRoles().stream()
                .collect(Collectors.toMap(Role::getRoleName, role -> role));
        log.info("Role cache initialized with {} roles", roleCache.size());
    }

    @Override
    public Role getRoleByName(String roleName) {
        return roleCache.computeIfAbsent(roleName, key -> {
            log.error("Role not found: {}", key);
            throw new RoleNotFoundException(key);
        });
    }
}
