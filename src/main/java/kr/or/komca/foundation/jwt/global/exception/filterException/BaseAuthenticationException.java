package kr.or.komca.foundation.jwt.global.exception.filterException;


import kr.or.komca.foundation.jwt.domain.dto.response.FilterErrorResponse;
import kr.or.komca.komcacommoninterface.dto.BaseResponse;
import kr.or.komca.komcacommoninterface.exception.BaseException;
import kr.or.komca.komcacommoninterface.response_code.ErrorCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.util.List;


@Getter
public abstract class BaseAuthenticationException extends RuntimeException implements BaseException {
	private final ErrorCode errorCode;

	// 추가 정보를 받는 생성자
	protected BaseAuthenticationException(ErrorCode errorCode) {
		super(errorCode.getCode());
		this.errorCode = errorCode;
	}

	public ResponseEntity<BaseResponse> toFilterErrorResponse() {
		FilterErrorResponse.ErrorDetail errorDetail = FilterErrorResponse.ErrorDetail.builder()
				.code(errorCode.getCode())
				.build();

		return FilterErrorResponse.of(errorCode, List.of(errorDetail));
	}
}