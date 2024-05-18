package com.crewing.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignUpDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BasicSignUpRequest {

        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String name;
        @NotBlank
        private String password;
        @NotBlank
        private String nickname;
        @NotNull
        private int age;
        @NotBlank
        private String city;
        @NotBlank
        private String school;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class OauthSignUpRequest {


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponse {
        private String accessToken;
        private String refreshToken;

        @JsonIgnore
        private final ObjectMapper objectMapper = new ObjectMapper();

        public String convertToJson() throws JsonProcessingException {
            return objectMapper.writeValueAsString(this);
        }

    }


}
