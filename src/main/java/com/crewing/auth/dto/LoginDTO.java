package com.crewing.auth.dto;

import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LoginDTO {

    /**
     * Request
     */

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OauthLoginRequest {
        @Schema(description = "Oauth 인증용 토큰", example = "eyj....")
        private String oauthAccessToken;
        @Schema(description = "애플 로그인용 인가 코드")
        private String authorizationCode;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @Schema(description = "이메일", example = "gosu@gosu.com")
        @Email
        private String email;
        @Schema(description = "비밀번호", example = "gosugosu")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailLoginRequest {
        @Schema(description = "이메일", example = "gosu@gosu.com")
        @Email
        private String email;
        @Schema(description = "인증번호", example = "123456")
        private String authNumber;
    }

    /**
     * Response
     */
    
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class LoginResponse {
        @Schema(description = "회원가입 필요여뷰", example = "true")
        private boolean needSignUp;

        private TokenResponse tokenResponse;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmailLoginResponse {
        @Schema(description = "메일인증 성공여뷰", example = "true")
        private boolean verifyResult;

        @Schema(description = "회원가입 필요여뷰", example = "true")
        private boolean needSignUp;

        private TokenResponse tokenResponse;
    }


}
