package kr.or.komca.foundation.jwt.service.command;

import kr.or.komca.foundation.jwt.dto.command.request.LoginRequest;
import kr.or.komca.foundation.jwt.dto.command.request.SignupRequest;
import kr.or.komca.foundation.jwt.dto.command.request.TokenRefreshRequest;
import kr.or.komca.foundation.jwt.dto.command.response.TokenResponse;
import kr.or.komca.foundation.jwt.dto.command.response.UserResponse;
import kr.or.komca.foundation.jwt.exception.DuplicateUserException;
import kr.or.komca.foundation.jwt.exception.InvalidCredentialsException;
import kr.or.komca.foundation.jwt.exception.InvalidRefreshTokenException;
import kr.or.komca.foundation.jwt.exception.UserNotFoundException;

/**
 * 인증 관련 명령 처리 서비스 인터페이스
 * 사용자 등록, 로그인, 토큰 갱신 등의 명령을 처리
 */
public interface AuthCommandService {

    /**
     * 새로운 사용자 등록을 처리
     *
     * @param request 회원가입 요청 정보 ({@link SignupRequest})
     * @return 생성된 사용자 정보 ({@link UserResponse})
     * @throws DuplicateUserException 동일한 username이 이미 존재하는 경우
     */
    UserResponse signup(SignupRequest request);

    /**
     * 사용자 로그인을 처리하고 JWT 토큰을 발급
     *
     * @param request 로그인 요청 정보 ({@link LoginRequest})
     * @return 토큰 정보 ({@link TokenResponse}) - 액세스 토큰, 리프레시 토큰 포함
     * @throws InvalidCredentialsException 인증 정보가 유효하지 않은 경우
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    TokenResponse login(LoginRequest request);

    /**
     * 리프레시 토큰을 사용하여 새로운 액세스 토큰을 발급
     *
     * @param request 토큰 갱신 요청 정보 ({@link TokenRefreshRequest})
     * @return 새로운 토큰 정보 ({@link TokenResponse}) - 새로운 액세스 토큰 포함
     * @throws InvalidRefreshTokenException 리프레시 토큰이 유효하지 않은 경우
     * @throws UserNotFoundException 사용자를 찾을 수 없는 경우
     */
    TokenResponse refreshToken(TokenRefreshRequest request);
}