package com.crewing.member.entity;

import com.crewing.club.entity.Club;
import com.crewing.member.dto.MemberInfoResponse;
import com.crewing.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "club_id")
    private Club club;

    @NotBlank
    private Role role;

    public MemberInfoResponse toMemberInfoResponse(){
        MemberInfoResponse.UserInfo userInfo = MemberInfoResponse.UserInfo.builder()
                .userId(this.user.getId())
                .imageUrl(this.user.getProfileImage())
                .nickname(this.user.getNickname())
                .build();

        return MemberInfoResponse.builder()
                .memberId(this.memberId)
                .user(userInfo)
                .clubId(this.club.getClubId())
                .role(this.role)
                .build();
    }
}
