package com.crewing.mail;

import com.example.jolvre.auth.dto.SignUpDTO.TokenResponse;
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
        private String email;

        @NotEmpty(message = "인증 번호를 입력해 주세요")
        private String authNum;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SignUpEmailVerifyResponse {
        private String email;

        private boolean verifyResult;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class FindPwEmailVerifyResponse {
        private String email;
        private boolean verifyResult;
        private TokenResponse tokenResponse;
    }

}
