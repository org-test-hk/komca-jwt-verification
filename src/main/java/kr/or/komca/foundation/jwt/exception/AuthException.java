package kr.or.komca.foundation.jwt.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 인증 관련 기본 예외 클래스
 */
@Getter
public abstract class AuthException extends RuntimeException {

    /** 에러 코드 */
    private final String errorCode;

    /** HTTP 상태 코드 */
    private final HttpStatus status;

    /**
     * 기본 생성자
     * @param message 에러 메시지
     * @param errorCode 에러 코드
     * @param status HTTP 상태 코드
     */
    protected AuthException(String message, String errorCode, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    /**
     * 원인 예외를 포함한 생성자
     * @param message 에러 메시지
     * @param errorCode 에러 코드
     * @param status HTTP 상태 코드
     * @param cause 원인 예외
     */
    protected AuthException(String message, String errorCode, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.status = status;
    }
}