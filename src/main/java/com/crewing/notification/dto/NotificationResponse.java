package com.crewing.notification.dto;

import com.crewing.notification.entity.NotificationType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {
    private Long notificationId;
    private String message;
    private String createdDate;
    private String title;
    private String content;
    private ClubInfo clubInfo;
    private boolean isCheck;
    private NotificationType type;

    @Getter
    @Builder
    public static class ClubInfo{
        private Long clubId;
        private String name;
        private String profile;
    }
}
