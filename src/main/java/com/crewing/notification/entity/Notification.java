package com.crewing.notification.entity;

import com.crewing.club.entity.Club;
import com.crewing.common.entity.BaseTimeEntity;
import com.crewing.notification.dto.NotificationResponse;
import com.crewing.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NotificationMessage message; // 목록 메세지

    @Embedded
    private NotificationTitle title; // 제목

    private String content; // 자세한 내용

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "club_id")
    private Club club;

    @Column(nullable = false)
    private Boolean isCheck;

    public Boolean getCheck() {
        return this.isCheck;
    }

    public NotificationResponse toNotificationResponse() {
        return NotificationResponse.builder()
                .notificationId(this.getId())
                .clubInfo(NotificationResponse.ClubInfo.builder()
                        .clubId(this.club.getClubId())
                        .name(this.club.getName())
                        .profile(this.club.getProfile())
                        .build())
                .createdDate(this.getCreatedDate().toString())
                .isCheck(this.getCheck())
                .message(this.getMessage().getMessage())
                .content(this.getContent())
                .title(this.getTitle().getTitle())
                .build();
    }
}
