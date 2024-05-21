package com.crewing.user.dto;

import com.crewing.user.entity.Role;
import com.crewing.user.entity.SocialType;
import com.crewing.user.entity.User;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
        private String imageUrl;
        private Role role;
        private SocialType socialType;
        private String socialId;
        private String birth;
        private String gender;
        private String name;

        public static UserInfoResponse toDTO(User user) {
            return new UserInfoResponse(user.getId(), user.getEmail(), user.getPassword(), user.getNickname(),
                    user.getImageUrl(), user.getRole(), user.getSocialType(), user.getSocialId(), user.getBirth(),
                    user.getGender(), user.getName());
        }
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    public static class UserInfoResponses {
        private List<UserInfoResponse> responses;

        public static UserInfoResponses toDTO(List<User> users) {
            List<UserInfoResponse> responses = new ArrayList<>();

            users.forEach(user -> responses.add(UserInfoResponse.toDTO(user)));

            return new UserInfoResponses(responses);
        }
    }
}
