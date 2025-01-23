package kr.or.komca.foundation.jwt.global.exception.filterException;



import kr.or.komca.foundation.jwt.global.exception.ErrorCode.AuthErrorCode;
import kr.or.komca.komcadatacore.dto.common.CommonResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;


// 기본 인증 예외
@Getter
public abstract class BaseAuthenticationException extends RuntimeException {
	private final AuthErrorCode errorCode;

	protected BaseAuthenticationException(AuthErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public CommonResponse<Void> toResponse() {
		return CommonResponse.<Void>builder()
				.status(HttpStatus.UNAUTHORIZED.value())
				.code(errorCode.getCode())
				.errorDetail(errorCode.getMessage())
				.build();
	}
}