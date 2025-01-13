package kr.or.komca.foundation.jwt.service.command;

import kr.or.komca.foundation.jwt.domain.entity.Permission;
import kr.or.komca.foundation.jwt.mapper.command.PermissionCommandMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 권한 관련 명령을 처리하는 서비스 구현체
 * 권한의 생성, 수정, 삭제와 역할-권한 매핑을 관리
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PermissionCommandServiceImpl implements PermissionCommandService {
    private final PermissionCommandMapper permissionCommandMapper;

    @Override
    public void addPermissionToRole(Long roleNo, Long permissionNo) {
        log.debug("Adding permission {} to role {}", permissionNo, roleNo);
        permissionCommandMapper.insertRolePermission(roleNo, permissionNo);
    }

    @Override
    public void removePermissionFromRole(Long roleNo, Long permissionNo) {
        log.debug("Removing permission {} from role {}", permissionNo, roleNo);
        permissionCommandMapper.deleteRolePermission(roleNo, permissionNo);
    }

    @Override
    public Permission createPermission(Permission permission) {
        log.debug("Creating new permission: {}", permission);
        permissionCommandMapper.insertPermission(permission);
        return permission;
    }
}