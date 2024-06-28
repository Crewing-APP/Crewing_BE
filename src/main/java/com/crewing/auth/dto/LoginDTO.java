package com.crewing.auth.dto;

import com.crewing.auth.dto.SignUpDTO.TokenResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LoginDTO {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OauthLoginRequest {
        @Schema(description = "Oauth 인증용 토큰" , example = "eyj....")
        private String oauthAccessToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        @Schema(description = "이메일" , example = "gosu@gosu.com")
        private String email;
        @Schema(description = "비밀번호" , example = "gosugosu")
        private String password;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OauthLoginResponse {
        private TokenResponse tokenResponse;
        @Schema(description = "Oauth 회원가입 필요여뷰" , example = "true")
        private boolean needSignUp;
    }
}
