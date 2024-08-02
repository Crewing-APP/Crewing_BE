package com.crewing.member.dto;

import com.crewing.member.entity.Role;
import lombok.*;

@Getter
@Builder
public class MemberInfoResponse {
    private Long memberId;
    private UserInfo user;
    private Long clubId;
    private Role role;

    @Getter
    @Builder
    public static class UserInfo{
        private Long userId;
        private String name;
        private String imageUrl;
    }
}
