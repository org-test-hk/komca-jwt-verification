//package kr.or.komca.foundation.jwt.common.response;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Builder;
//import lombok.Getter;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
///**
// * API 응답을 위한 공통 응답 객체
// * @param <T> 응답 데이터의 타입
// */
//@Getter
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@Builder
//@Schema(description = "공통 응답 객체")
//public class CommonResponseFoundation<T> {
//    @Schema(description = "HTTP 상태 코드")
//    private final int status;
//
//    @Schema(description = "응답 코드")
//    private final String code;
//
//    @Schema(description = "응답 데이터")
//    private final T data;
//
//    @Schema(description = "에러 상세 정보")
//    private final Object message;
//
//    private CommonResponseFoundation(int status, String code, T data, Object errorDetail) {
//        this.status = status;
//        this.code = code;
//        this.data = data;
//        this.message = errorDetail;
//    }
//
//    public static <T> ResponseEntity<CommonResponseFoundation<T>> ok(T data) {
//        return ResponseEntity.ok(new CommonResponseFoundation<>(HttpStatus.OK.value(), null, data, null));
//    }
//
//    public static <T> ResponseEntity<CommonResponseFoundation<T>> created(T data) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(new CommonResponseFoundation<>(HttpStatus.CREATED.value(), null, data, null));
//    }
//
//    public static <T> ResponseEntity<CommonResponseFoundation<T>> error(HttpStatus status, String code) {
//        return ResponseEntity.status(status)
//                .body(new CommonResponseFoundation<>(status.value(), code, null, null));
//    }
//
//    public static <T> ResponseEntity<CommonResponseFoundation<T>> error(HttpStatus status, String code, Object errorDetail) {
//        return ResponseEntity.status(status)
//                .body(new CommonResponseFoundation<>(status.value(), code, null, errorDetail));
//    }
//}