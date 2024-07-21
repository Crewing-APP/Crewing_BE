package com.crewing.auth.dto;

import com.crewing.user.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

public class SignUpDTO {

    /**
     * Request
     */

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BasicSignUpRequest {
        @Schema(description = "생년 월일", example = "2000-05-04")
        private String birth;

        @Schema(description = "성별", example = "남자")
        private String gender;

        @Schema(description = "이름", example = "고수")
        private String name;

        @Schema(description = "닉네임", example = "반달곰수")
        private String nickname;

        @Schema(description = "관심항목", example = "[\"개발\",\"축구\"]")
        private List<String> interests;

        @Schema(description = "이메일", example = "gosu@gosu.com")
        @Email
        private String email;

        @Schema(description = "이메일 인증 여부", example = "true")
        private boolean verified;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class OauthSignUpRequest {
        @Schema(description = "생년 월일", example = "2000-05-04")
        private String birth;

        @Schema(description = "성별", example = "남자")
        private String gender;

        @Schema(description = "이름", example = "고수")
        private String name;

        @Schema(description = "닉네임", example = "반달곰수")
        private String nickname;

        @Schema(description = "관심항목", example = "[\"개발\",\"축구\"]")
        private List<String> interests;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class RefreshRequest {
        @Schema(description = "재발급용 토큰", example = "eyj....")
        private String refreshToken;
    }


    /**
     * Response
     */

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TokenResponse {

        @Schema(description = "인증용 토큰", example = "eyj....")
        private String accessToken;

        @Schema(description = "재발급용 토큰", example = "eyj....")
        private String refreshToken;

        @Schema(description = "유저 권한", example = "ADMIN")
        private Role role;

        @JsonIgnore
        private final ObjectMapper objectMapper = new ObjectMapper();

        public String convertToJson() throws JsonProcessingException {
            return objectMapper.writeValueAsString(this);
        }

    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class EmailDuplicateCheckResponse {
        @Schema(description = "이메일", example = "gomsu@gomsu.gomsu")
        private String email;

        @Schema(description = "중복여부 true -> 중복", example = "true")
        private boolean duplicate;
    }

}
