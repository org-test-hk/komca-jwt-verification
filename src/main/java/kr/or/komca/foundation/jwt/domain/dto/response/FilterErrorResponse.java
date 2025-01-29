package kr.or.komca.foundation.jwt.domain.dto.response;


import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import kr.or.komca.komcacommoninterface.response_code.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
public class FilterErrorResponse extends BaseResponse<Void> {

    protected FilterErrorResponse() {
        super();  // 기본 생성자 호출
    }

    private FilterErrorResponse(ErrorCode errorCode, Object errorDetail) {
        super(errorCode, null, errorDetail);
    }

    public static ResponseEntity<BaseResponse<Void>> of(ErrorCode errorCode) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", errorCode.getCode());
        return ResponseEntity.status(errorCode.getStatus())
                .body(new FilterErrorResponse(errorCode, errors));
    }

    public static ResponseEntity<BaseResponse<Void>> of(ErrorCode errorCode, Object errorDetail) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(new FilterErrorResponse(errorCode, errorDetail));
    }
}
