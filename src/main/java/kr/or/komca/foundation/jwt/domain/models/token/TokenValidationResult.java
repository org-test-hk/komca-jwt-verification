package kr.or.komca.foundation.jwt.domain.models.token;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 토큰 검증 결과를 담는 불변 DTO
 */
@Getter
@Builder
public class TokenValidationResult {
    private final boolean valid;
    private final String errorMessage;

    @Builder.Default
    private final List<String> warnings = new ArrayList<>();

    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    public static TokenValidationResult withWarning(String warning) {
        return TokenValidationResult.builder()
                .valid(true)
                .warnings(new ArrayList<>(List.of(warning)))
                .build();
    }

    public static TokenValidationResult invalid(String errorMessage) {
        return TokenValidationResult.builder()
                .valid(false)
                .errorMessage(errorMessage)
                .build();
    }

    public static TokenValidationResult valid() {
        return TokenValidationResult.builder()
                .valid(true)
                .build();
    }
}
