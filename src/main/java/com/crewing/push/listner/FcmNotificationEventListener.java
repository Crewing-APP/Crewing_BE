package com.crewing.push.listner;

import com.crewing.device.service.DeviceService;
import com.crewing.push.dto.FcmDto.FcmNotificationEvent;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class FcmNotificationEventListener {
    private final DeviceService deviceService;
    private final FirebaseMessaging firebaseMessaging;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void sendMessage(FcmNotificationEvent event) {
        List<String> tokens = deviceService.getFcmTokensByUserIds(event.getUserIds()).getFcmTokens();

        Notification notification = Notification.builder()
                .setTitle(event.getTitle())
                .setBody(event.getBody())
                .build();

        MulticastMessage messages = MulticastMessage.builder()
                .addAllTokens(tokens)
                .setNotification(notification)
                .build();

        firebaseMessaging.sendEachForMulticastAsync(messages);
    }
}
