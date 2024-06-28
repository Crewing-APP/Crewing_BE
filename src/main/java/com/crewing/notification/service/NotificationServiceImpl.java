package com.crewing.notification.service;

import com.crewing.common.error.notification.NotificationNotFoundException;
import com.crewing.notification.dto.NotificationResponse;
import com.crewing.notification.dto.NotificationListResponse;
import com.crewing.notification.entity.Notification;
import com.crewing.notification.repository.NotificationRepository;
import com.crewing.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService{
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public NotificationListResponse getAllNotificationInfo(User user, Pageable pageable) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"id"));
        Page<Notification> notifications = notificationRepository.findAllByReceiver(user,pageRequest);
        List<NotificationResponse> responseList = notifications.map(Notification::toNotificationResponse).stream().toList();

        return NotificationListResponse.builder()
                .pageNum(notifications.getNumber())
                .totalCnt(notifications.getTotalElements())
                .pageSize(notifications.getSize())
                .notifications(responseList)
                .build();
    }

    @Override
    @Transactional
    public void checkNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(NotificationNotFoundException::new);
        notificationRepository.save(notification.toBuilder()
                        .isCheck(true)
                        .build());
    }

    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow(NotificationNotFoundException::new);
        notificationRepository.delete(notification);
    }
}
