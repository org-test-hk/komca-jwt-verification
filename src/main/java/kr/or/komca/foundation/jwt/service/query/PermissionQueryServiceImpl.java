package kr.or.komca.foundation.jwt.service.query;

import kr.or.komca.foundation.jwt.domain.entity.Permission;
import kr.or.komca.foundation.jwt.mapper.query.PermissionQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 권한 조회 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionQueryServiceImpl implements PermissionQueryService {
    private final PermissionQueryMapper permissionQueryMapper;

    @Override
    public List<Permission> getPermissionsByRoleNo(Long roleNo) {
        log.debug("Retrieving permissions for role: {}", roleNo);
        return permissionQueryMapper.findByRoleNo(roleNo);
    }

    @Override
    public List<Permission> getAllPermissions() {
        log.debug("Retrieving all permissions");
        return permissionQueryMapper.findAll();
    }
}