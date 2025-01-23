package kr.or.komca.foundation.jwt.global.exception.asis;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 리프레시 토큰이 유효하지 않을 때 발생하는 예외
 * - 토큰이 만료된 경우
 * - 토큰이 DB에 존재하지 않는 경우
 * - 토큰 형식이 잘못된 경우
 */
@Getter
public class InvalidRefreshTokenException extends AuthException {

    /** 기본 에러 메시지 */
    private static final String DEFAULT_MESSAGE = "유효하지 않은 리프레시 토큰입니다";

    /** 기본 에러 코드 */
    private static final String ERROR_CODE = "INVALID_REFRESH_TOKEN";

    /** HTTP 상태 코드 */
    private static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    /**
     * 기본 생성자
     */
    public InvalidRefreshTokenException() {
        super(DEFAULT_MESSAGE, ERROR_CODE, STATUS);
    }

    /**
     * 상세 메시지를 포함한 생성자
     * @param message 상세 에러 메시지
     */
    public InvalidRefreshTokenException(String message) {
        super(message, ERROR_CODE, STATUS);
    }

    /**
     * 원인 예외를 포함한 생성자
     * @param message 상세 에러 메시지
     * @param cause 원인 예외
     */
    public InvalidRefreshTokenException(String message, Throwable cause) {
        super(message, ERROR_CODE, STATUS, cause);
    }
}