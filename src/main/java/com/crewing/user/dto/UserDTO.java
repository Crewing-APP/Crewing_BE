package com.crewing.user.dto;

import com.crewing.user.entity.Role;
import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

public class UserDTO {

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class UserInfoResponse {
        private Long id;
        private String email;
        private String password;
        private String nickname;
        private String profileImage;
        private Role role;
        private SocialType socialType;
        private String socialId;
        private String birth;
        private String gender;
        private String name;
        private LocalDate deleteAt;
        private List<InterestInfo> interests;
        private int point;

        public static UserInfoResponse toDTO(User user) {
            return new UserInfoResponse(user.getId(), user.getEmail(), user.getPassword(), user.getNickname(),
                    user.getProfileImage(), user.getRole(), user.getSocialType(), user.getSocialId(), user.getBirth(),
                    user.getGender(), user.getName(), user.getDeleteAt(),
                    user.getInterests().stream()
                            .map(data -> InterestInfo.builder().interest(data.getInterest()).build())
                            .toList(), user.getPoint()
            );
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class UserInfoResponses {
        private List<UserInfoResponse> responses;

        public static UserInfoResponses toDTO(List<User> users) {
            List<UserInfoResponse> responses = users.stream().map(UserInfoResponse::toDTO).toList();
            return new UserInfoResponses(responses);
        }
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class InterestUpdateRequest {
        private List<InterestInfo> interests;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class InterestInfo {
        @Schema(description = "관심사", example = "축구")
        private String interest;
    }

}
