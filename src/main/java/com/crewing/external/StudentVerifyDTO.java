package com.crewing.external;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class StudentVerifyDTO {

    @Getter
    @Builder
    @AllArgsConstructor
    public static class StudentCreateVerificationRequest {
        @Schema(description = "이메일", example = "gosu@gosu.com")
        private final String email;
        @Schema(description = "대학교 이름", example = "크루잉짱짱대학교")
        private final String universityName;

        @Schema(hidden = true)
        private final String mailTitle = "Crewing";
    }

    @Getter
    @Builder
    @AllArgsConstructor
    public static class StudentVerifyRequest {
        @Schema(description = "이메일", example = "gosu@gosu.com")
        private final String email;
        @Schema(description = "대학교 이름", example = "크루잉짱짱대학교")
        private final String universityName;
        @Schema(description = "인증코드", example = "123456")
        private final String authCode;
    }
}
