package kr.or.komca.foundation.jwt.global.exception.filterException;


import jakarta.servlet.http.HttpServletResponse;
import kr.or.komca.foundation.jwt.domain.dto.response.FilterErrorResponse;
import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import kr.or.komca.komcacommoninterface.exception.BaseException;
import kr.or.komca.komcacommoninterface.response_code.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;


// 기본 인증 예외
//@Getter
//public abstract class BaseAuthenticationException extends RuntimeException {
//	private final AuthErrorCode errorCode;
//
//	protected BaseAuthenticationException(AuthErrorCode errorCode) {
//		super(errorCode.getMessage());
//		this.errorCode = errorCode;
//	}
//
//	public CommonResponse<Void> toResponse() {
//		return CommonResponse.<Void>builder()
//				.status(HttpStatus.UNAUTHORIZED.value())
//				.code(errorCode.getCode())
//				.errorDetail(errorCode.getMessage())
//				.build();
//	}
//}

@Getter
public abstract class BaseAuthenticationException extends RuntimeException implements BaseException {
	private final ErrorCode errorCode;
	private final Map<String, Object> params;  // 추가 정보를 위한 필드

	// 기본 생성자
	protected BaseAuthenticationException(ErrorCode errorCode) {
		this(errorCode, null);
	}

	// 추가 정보를 받는 생성자
	protected BaseAuthenticationException(ErrorCode errorCode, Map<String, Object> params) {
		super(errorCode.getCode());
		this.errorCode = errorCode;
		this.params = params;
	}

	public ResponseEntity<BaseResponse<Void>> toResponseEntity() {
		BaseResponse.ErrorDetail errorDetail = BaseResponse.ErrorDetail.builder()
				.code(errorCode.getCode())
				.params(params)  // 추가 정보 포함
				.build();

		return FilterErrorResponse.of(errorCode, List.of(errorDetail));
	}

	public void toResponseJSON(HttpServletResponse response) throws IOException {
		BaseResponse<Void> errorResponse = toResponseEntity().getBody();
		if (errorResponse != null) {
			errorResponse.toResponseJSON(response);
		}
	}
}