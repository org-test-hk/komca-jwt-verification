package kr.or.komca.foundation.jwt.dto.command.response;

import kr.or.komca.foundation.jwt.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 사용자 정보 응답 DTO
 */
@Getter
@Builder
@ToString
public class UserResponse {
    /** 사용자 번호 */
    private final Long userNo;

    /** 사용자 아이디 */
    private final String username;

    /** 이름 */
    private final String name;

    /** 이메일 */
    private final String email;

    /** 역할 목록 */
    private final List<RoleResponse> roles;

    /**
     * User 엔티티로부터 DTO 생성
     */
    public static UserResponse from(User user) {
        return UserResponse.builder()
                .userNo(user.getUserNo())
                .username(user.getUsername())
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(RoleResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}