package com.crewing.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SignUpDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OauthSignUpRequest {
        private String birth;
        private String gender;
        private String name;
        private String nickname;
        private List<String> interests;
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
