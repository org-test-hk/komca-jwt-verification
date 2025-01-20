//package kr.or.komca.foundation.jwt.exception.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.JwtException;
//import kr.or.komca.foundation.jwt.common.response.CommonResponseFoundation;
//import kr.or.komca.foundation.jwt.exception.asis.AuthException;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.validation.BindException;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 인증/인가 관련 예외 처리를 담당하는 전역 예외 핸들러
// */
//@Slf4j
//@RestControllerAdvice
//@RequiredArgsConstructor
//public class AuthExceptionHandler {
//
//    private final ObjectMapper objectMapper;
//
//    /**
//     * AuthException 하위 클래스들의 공통 예외 처리
//     * @param e AuthException
//     * @return 에러 응답
//     */
//    @ExceptionHandler(AuthException.class)
//    public ResponseEntity<CommonResponseFoundation<Void>> handleAuthException(AuthException e) {
//        log.error("Auth exception occurred: {}", e.getMessage());
//        return CommonResponseFoundation.error(
//                e.getStatus(),
//                e.getErrorCode(),
//                e.getMessage()
//        );
//    }
//
//    /**
//     * JWT 관련 예외 처리
//     * @param e JwtException
//     * @return 에러 응답
//     */
//    @ExceptionHandler(JwtException.class)
//    public ResponseEntity<CommonResponseFoundation<Void>> handleJwtException(JwtException e) {
//        log.error("JWT exception occurred: {}", e.getMessage());
//        return CommonResponseFoundation.error(
//                HttpStatus.UNAUTHORIZED,
//                "INVALID_TOKEN",
//                "유효하지 않은 토큰입니다"
//        );
//    }
//
//    /**
//     * 접근 권한 관련 예외 처리
//     * @param e AccessDeniedException
//     * @return 에러 응답
//     */
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<CommonResponseFoundation<Void>> handleAccessDeniedException(AccessDeniedException e) {
//        log.error("Access denied: {}", e.getMessage());
//        return CommonResponseFoundation.error(
//                HttpStatus.FORBIDDEN,
//                "ACCESS_DENIED",
//                "접근 권한이 없습니다"
//        );
//    }
//
//    /**
//     * 인증 실패 예외 처리
//     * @param e AuthenticationException
//     * @return 에러 응답
//     */
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<CommonResponseFoundation<Void>> handleAuthenticationException(AuthenticationException e) {
//        log.error("Authentication failed: {}", e.getMessage());
//        return CommonResponseFoundation.error(
//                HttpStatus.UNAUTHORIZED,
//                "AUTHENTICATION_FAILED",
//                "인증에 실패했습니다"
//        );
//    }
//
//    /**
//     * 잘못된 자격 증명 예외 처리
//     * @param e BadCredentialsException
//     * @return 에러 응답
//     */
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<CommonResponseFoundation<Void>> handleBadCredentialsException(BadCredentialsException e) {
//        log.error("Bad credentials: {}", e.getMessage());
//        return CommonResponseFoundation.error(
//                HttpStatus.UNAUTHORIZED,
//                "INVALID_CREDENTIALS",
//                "아이디 또는 비밀번호가 올바르지 않습니다"
//        );
//    }
//
//    /**
//     * 요청 파라미터 검증 실패 예외 처리
//     * @param e MethodArgumentNotValidException
//     * @return 필드 별 에러 메시지를 포함한 응답
//     */
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<CommonResponseFoundation<Map<String, String>>> handleValidationExceptions(
//            MethodArgumentNotValidException e) {
//        Map<String, String> errors = e.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .collect(Collectors.toMap(
//                        FieldError::getField,
//                        error -> error.getDefaultMessage() != null ? error.getDefaultMessage() : "invalid value",
//                        (existing, replacement) -> existing
//                ));
//
//        log.error("Validation failed: {}", errors);
//
//        return CommonResponseFoundation.error(
//                HttpStatus.BAD_REQUEST,
//                "VALIDATION_ERROR",
//                errors
//        );
//    }
//
//    /**
//     * 바인딩 실패 예외 처리
//     * @param e BindException
//     * @return 에러 응답
//     */
//    @ExceptionHandler(BindException.class)
//    public ResponseEntity<CommonResponseFoundation<Void>> handleBindException(BindException e) {
//        log.error("Binding failed: {}", e.getMessage());
//        return CommonResponseFoundation.error(
//                HttpStatus.BAD_REQUEST,
//                "BINDING_ERROR",
//                "요청 파라미터가 올바르지 않습니다"
//        );
//    }
//
//    /**
//     * 예상치 못한 예외 처리
//     * @param e Exception
//     * @return 에러 응답
//     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<CommonResponseFoundation<Void>> handleUnexpectedException(Exception e) {
//        log.error("Unexpected error occurred", e);
//        return CommonResponseFoundation.error(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "INTERNAL_SERVER_ERROR",
//                "서버 내부 오류가 발생했습니다"
//        );
//    }
//}