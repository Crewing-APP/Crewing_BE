package com.crewing.notification.service;

import com.crewing.notification.dto.NotificationListResponse;
import com.crewing.user.entity.User;
import org.springframework.data.domain.Pageable;

public interface NotificationService {
    NotificationListResponse getAllNotificationInfo(User user, Pageable pageable);
    void checkNotification(Long notificationId);
    void deleteNotification(Long notificationId);
}
