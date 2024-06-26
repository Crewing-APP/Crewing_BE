package com.crewing.notification.dto;

import com.crewing.club.entity.Club;
import com.crewing.notification.entity.Notification;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NotificationApplyResponse {
    private Long notificationId;
    private String message;
    private String createdDate;

    private ClubInfo clubInfo;
    private boolean isCheck;

    @Getter
    @Builder
    public static class ClubInfo{
        private Long clubId;
        private String name;
        private String profile;
    }
}
