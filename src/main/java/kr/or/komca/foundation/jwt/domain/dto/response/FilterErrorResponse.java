package kr.or.komca.foundation.jwt.domain.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import kr.or.komca.komcacommoninterface.errorcode.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;

import java.util.List;

@Getter
public class FilterErrorResponse extends BaseResponse {

    private final List<ErrorDetail> errorDetails;
    private final String code;

    private FilterErrorResponse(ErrorCode errorCode, List<ErrorDetail> errorDetails) {
        super(errorCode.getStatusCode());
        this.errorDetails = errorDetails;
        this.code = errorCode.getCode();
    }

    public static ResponseEntity<BaseResponse> from(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new FilterErrorResponse(
                        errorCode,
                        List.of(new ErrorDetail(errorCode.getCode(), null)))
                );
    }

    public static ResponseEntity<BaseResponse> of(ErrorCode errorCode, List<ErrorDetail> errorDetails) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new FilterErrorResponse(errorCode, errorDetails));
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {
        private final String code;         // 에러 상세 코드
        private final Object value;        // 실제 입력값 (옵션)
    }
}
