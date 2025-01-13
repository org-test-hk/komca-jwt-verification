package kr.or.komca.foundation.jwt.service.command;

import kr.or.komca.foundation.jwt.domain.entity.Permission;
import kr.or.komca.foundation.jwt.domain.entity.Role;
import kr.or.komca.foundation.jwt.domain.entity.User;
import kr.or.komca.foundation.jwt.dto.command.request.LoginRequest;
import kr.or.komca.foundation.jwt.dto.command.request.SignupRequest;
import kr.or.komca.foundation.jwt.dto.command.request.TokenRefreshRequest;
import kr.or.komca.foundation.jwt.dto.command.response.TokenResponse;
import kr.or.komca.foundation.jwt.dto.command.response.UserResponse;
import kr.or.komca.foundation.jwt.exception.DuplicateUserException;
import kr.or.komca.foundation.jwt.exception.InvalidCredentialsException;
import kr.or.komca.foundation.jwt.exception.InvalidRefreshTokenException;
import kr.or.komca.foundation.jwt.exception.UserNotFoundException;
import kr.or.komca.foundation.jwt.mapper.command.PermissionCommandMapper;
import kr.or.komca.foundation.jwt.mapper.command.UserCommandMapper;
import kr.or.komca.foundation.jwt.mapper.query.PermissionQueryMapper;
import kr.or.komca.foundation.jwt.mapper.query.UserQueryMapper;
import kr.or.komca.foundation.jwt.security.jwt.JwtTokenProvider;
import kr.or.komca.foundation.jwt.service.query.RoleQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * 인증 관련 명령 처리 서비스 구현체
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class AuthCommandServiceImpl implements AuthCommandService {

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserCommandMapper userCommandMapper;
    private final UserQueryMapper userQueryMapper;
    private final AuthenticationManager authenticationManager;
    private final RoleQueryService roleQueryService;
    private final PermissionCommandMapper permissionCommandMapper;
    private final PermissionQueryMapper permissionQueryMapper;

    /**
     * 회원가입 처리
     * @param request 회원가입 요청 정보
     * @return 생성된 사용자 정보
     * @throws DuplicateUserException 중복된 사용자가 존재하는 경우
     */
    @Override
    public UserResponse signup(SignupRequest request) {
        log.debug("Processing signup request for username: {}", request.getUsername());

        // 1. 중복 사용자 검증
        validateUniqueUsername(request.getUsername());

        // 2. 사용자 생성
        User user = createUser(request);
        log.debug("Created new user with ID: {}", user.getUserNo());

        // 3. 역할 및 권한 할당
        assignRolesAndPermissions(user, request);
        log.debug("Assigned roles and permissions for user: {}", user.getUserNo());

        // 4. 생성된 사용자 정보 조회 및 반환
        return userQueryMapper.findByUsername(request.getUsername())
                .map(UserResponse::from)
                .orElseThrow(() -> new UserNotFoundException(request.getUsername()));
    }

    /**
     * 사용자 아이디 중복 검증
     */
    private void validateUniqueUsername(String username) {
        userQueryMapper.findByUsername(username)
                .ifPresent(user -> {
                    log.error("Duplicate username found: {}", username);
                    throw new DuplicateUserException(username);
                });
    }

    /**
     * 사용자 엔티티 생성 및 저장
     */
    private User createUser(SignupRequest request) {
        User user = request.toEntity(passwordEncoder.encode(request.getPassword()));
        userCommandMapper.insertUser(user);
        return user;
    }

    /**
     * 사용자에게 역할과 권한을 할당
     * 부모 메소드의 트랜잭션 컨텍스트 내에서 실행
     */
    private void assignRolesAndPermissions(User user, SignupRequest request) {
        // 기본 역할(ROLE_USER) 할당
        Role defaultRole = roleQueryService.getRoleByName("ROLE_USER");
        userCommandMapper.insertUserRole(user.getUserNo(), defaultRole.getRoleNo());

        // 추가 역할 할당
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            for (String roleName : request.getRoles()) {
                Role role = roleQueryService.getRoleByName(roleName);
                userCommandMapper.insertUserRole(user.getUserNo(), role.getRoleNo());
            }
        }

        // 권한 할당
        if (request.getPermissions() != null && !request.getPermissions().isEmpty()) {
            for (String permissionName : request.getPermissions()) {
                Permission permission = permissionQueryMapper.findByPermissionName(permissionName)
                        .orElseThrow(() -> new IllegalArgumentException("Permission not found: " + permissionName));

                // 사용자의 모든 역할에 대해 권한 할당
                user.getRoles().forEach(role -> {
                    try {
                        permissionCommandMapper.insertRolePermission(role.getRoleNo(), permission.getPermissionNo());
                    } catch (Exception e) {
                        log.error("Failed to assign permission {} to role {}", permissionName, role.getRoleName(), e);
                        throw new RuntimeException("Failed to assign permissions", e);
                    }
                });
            }
        }
    }

    /**
     * 로그인 처리
     * @param request 로그인 요청 정보
     * @return 토큰 정보
     * @throws InvalidCredentialsException 인증 실패 시
     */
    @Override
    public TokenResponse login(LoginRequest request) {
        log.debug("Processing login request for username: {}", request.getUsername());

        try {
            // 인증 시도
            Authentication authentication = authenticate(request.getUsername(), request.getPassword());
            User user = getUserFromAuthentication(authentication);

            // 토큰 생성
            String accessToken = jwtTokenProvider.createAccessToken(
                    user.getUsername(),
                    user.getRoles().stream()
                            .map(Role::getRoleName)
                            .collect(Collectors.toList())
            );
            String refreshToken = jwtTokenProvider.createRefreshToken(user.getUsername());

            // 리프레시 토큰 저장
            saveRefreshToken(user.getUserNo(), refreshToken);

            log.debug("Login successful for user: {}", user.getUsername());

            return TokenResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                    .user(UserResponse.from(user))
                    .build();

        } catch (AuthenticationException e) {
            log.error("Authentication failed for username: {}", request.getUsername(), e);
            throw new InvalidCredentialsException();
        }
    }

    /**
     * 토큰 갱신
     * @param request 토큰 갱신 요청 정보
     * @return 새로운 토큰 정보
     * @throws InvalidRefreshTokenException 리프레시 토큰이 유효하지 않은 경우
     */
    @Override
    public TokenResponse refreshToken(TokenRefreshRequest request) {
        log.debug("Processing token refresh request");

        // 리프레시 토큰으로 사용자 조회
        User user = userQueryMapper.findByRefreshToken(request.getRefreshToken())
                .orElseThrow(() -> {
                    log.error("Invalid refresh token provided");
                    return new InvalidRefreshTokenException();
                });

        // 새로운 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(
                user.getUsername(),
                user.getRoles().stream()
                        .map(Role::getRoleName)
                        .collect(Collectors.toList())
        );

        log.debug("Token refresh successful for user: {}", user.getUsername());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(request.getRefreshToken())
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getAccessTokenValidityInSeconds())
                .user(UserResponse.from(user))
                .build();
    }

    /**
     * 사용자 인증 수행
     * @param username 사용자 아이디
     * @param password 비밀번호
     * @return 인증 객체
     * @throws AuthenticationException 인증 실패 시
     */
    private Authentication authenticate(String username, String password) {
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );
    }

    /**
     * Authentication 객체에서 사용자 정보 추출
     * @param authentication 인증 객체
     * @return 사용자 정보
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    private User getUserFromAuthentication(Authentication authentication) {
        return userQueryMapper.findByUsername(authentication.getName())
                .orElseThrow(() -> new UserNotFoundException(authentication.getName()));
    }

    /**
     * 리프레시 토큰 저장
     * @param userNo 사용자 번호
     * @param refreshToken 리프레시 토큰
     */
    private void saveRefreshToken(Long userNo, String refreshToken) {
        userCommandMapper.insertRefreshToken(
                userNo,
                refreshToken,
                LocalDateTime.now().plusSeconds(jwtTokenProvider.getRefreshTokenValidityInSeconds())
        );
    }
}