package com.crewing.notification.entity;

import com.crewing.club.entity.Club;
import com.crewing.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SSEEvent {
    private NotificationType notificationType;
    private User receiver;
    private String message;
    private String content;
    private Club club;
}
