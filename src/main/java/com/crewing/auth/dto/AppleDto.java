package com.crewing.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

public class AppleDto {
    @Getter
    @Builder
    @ToString
    public static class AppleTokenRequestDto {
        private String client_id;
        private String client_secret;
        private String code;
        private String grant_type;
        private String refresh_token;
    }

    @Getter
    @Builder
    public static class AppleRevokeRequest {
        private String client_id;
        private String client_secret;
        private String token;
        private String token_type_hint;
    }
}
