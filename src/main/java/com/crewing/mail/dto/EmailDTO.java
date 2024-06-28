package com.crewing.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class EmailDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class EmailSendResponse {
        private String email;
        private String authNum;
        private boolean isUser;
    }

    @Getter
    @AllArgsConstructor
    public static class EmailVerifyRequest {
        @Email
        @NotEmpty(message = "이메일을 입력해 주세요")
        @Schema(description = "이메일" , example = "gosu@gosu.com")
        private String email;

        @NotEmpty(message = "인증 번호를 입력해 주세요")
        @Schema(description = "인증번호 6자리" , example = "123123")
        private String authNum;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SignUpEmailVerifyResponse {
        @Schema(description = "이메일" , example = "gosu@gosu.com")
        private String email;

        private boolean verifyResult;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class FindPwEmailVerifyResponse {
        @Schema(description = "이메일" , example = "gosu@gosu.com")
        private String email;
        @Schema(description = "인증 결과" , example = "true")
        private boolean verifyResult;

    }

}
