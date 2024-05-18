package com.crewing.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LoginDTO {

    @Getter
    @AllArgsConstructor
    public static class OauthLoginRequest {
        private String accessToken;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginRequest {
        private String email;
        private String password;
    }
}
