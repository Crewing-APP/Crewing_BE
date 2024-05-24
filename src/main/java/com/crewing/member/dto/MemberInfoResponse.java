package com.crewing.member.dto;

import com.crewing.member.entity.Role;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberInfoResponse {
    private Long memberId;
    private UserInfo user;
    private Long clubId;
    private Role role;

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    private static class UserInfo{
        private Long userId;
        private String nickname;
        private String imageUrl;
    }
}
