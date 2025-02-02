package kr.or.komca.foundation.jwt.domain.dto.response;


import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import kr.or.komca.komcacommoninterface.response_code.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class FilterErrorResponse extends BaseResponse<Void> {


    private FilterErrorResponse(ErrorCode errorCode, List<ErrorDetail> errorDetails) {
        super(errorCode.getStatus().value(), errorCode.getCode(), errorDetails);
    }

    public static ResponseEntity<BaseResponse<Void>> of(ErrorCode errorCode) {
        BaseResponse.ErrorDetail errorDetail = BaseResponse.ErrorDetail.builder()
                .code(errorCode.getCode())
                .build();

        return of(errorCode, List.of(errorDetail));
    }

    public static ResponseEntity<BaseResponse<Void>> of(ErrorCode errorCode, Object errorData) {
        Map<String, Object> params = new HashMap<>();
        if (errorData != null) {
            if (errorData instanceof Map<?, ?> map) {  // Java 16+ pattern matching
                // 모든 키가 String인지 확인
                if (map.keySet().stream().allMatch(key -> key instanceof String)) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> typedMap = (Map<String, Object>) map;
                    params.putAll(typedMap);
                } else {
                    params.put("detail", errorData);
                }
            } else {
                params.put("detail", errorData);
            }
        }

        BaseResponse.ErrorDetail errorDetail = BaseResponse.ErrorDetail.builder()
                .code(errorCode.getCode())
                .params(params.isEmpty() ? null : params)
                .build();

        return of(errorCode, List.of(errorDetail));
    }

    private static ResponseEntity<BaseResponse<Void>> of(ErrorCode errorCode, List<ErrorDetail> errorDetails) {
        return ResponseEntity
                .status(errorCode.getStatus())
                .body(new FilterErrorResponse(errorCode, errorDetails));
    }
}
