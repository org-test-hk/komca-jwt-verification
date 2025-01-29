package kr.or.komca.foundation.jwt.global.exception.filterException;



import jakarta.servlet.http.HttpServletResponse;
import kr.or.komca.foundation.jwt.domain.dto.response.FilterErrorResponse;
import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;
import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import kr.or.komca.komcacommoninterface.exception.BaseException;
import kr.or.komca.komcacommoninterface.response_code.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.io.IOException;


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

	protected BaseAuthenticationException(ErrorCode errorCode) {
		super(errorCode.getCode());
		this.errorCode = errorCode;
	}

	public ResponseEntity<BaseResponse<Void>> toResponseEntity() {
		return FilterErrorResponse.of(errorCode);
	}

	public void toResponseJSON(HttpServletResponse response) throws IOException {
		BaseResponse<Void> errorResponse = FilterErrorResponse.of(errorCode).getBody();
		errorResponse.toResponseJSON(response);
	}
}