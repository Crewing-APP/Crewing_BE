package com.crewing.notification.service;

import com.crewing.club.entity.Club;
import com.crewing.notification.dto.NotificationResponse;
import com.crewing.notification.entity.Notification;
import com.crewing.notification.entity.NotificationMessage;
import com.crewing.notification.entity.NotificationTitle;
import com.crewing.notification.entity.NotificationType;
import com.crewing.notification.repository.EmitterRepository;
import com.crewing.notification.repository.NotificationRepository;
import com.crewing.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SSEService {
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;

    // 연결 지속시간 한 시간
    private static final Long DEFAULT_TIMEOUT = 60L * 60 * 1000;

    public SseEmitter subscribe(User user, String lastEventId) {
        // 고유 아이디 생성
        String emitterId = user.getId() + "_" + System.currentTimeMillis();

        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
        log.info("new emitter added : {}", sseEmitter);
        log.info("lastEventId : {}", lastEventId);

        /* 상황별 emitter 삭제 처리 */
        sseEmitter.onCompletion(() -> emitterRepository.deleteById(emitterId)); //완료 시, 타임아웃 시, 에러 발생 시
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));
        sseEmitter.onError((e) -> emitterRepository.deleteById(emitterId));

        /* 503 Service Unavailable 방지용 dummy event 전송 */
        sendToClient(sseEmitter, emitterId, "EventStream Created. [userId=" + user.getId() + "]");

        /* client가 미수신한 event 목록이 존재하는 경우 */
        if(!lastEventId.isEmpty()) { //client가 미수신한 event가 존재하는 경우 이를 전송하여 유실 예방
            Map<String, Object> eventCaches = emitterRepository.findAllEventCacheStartWithByUserId(String.valueOf(user.getId())); //id에 해당하는 eventCache 조회
            eventCaches.entrySet().stream() //미수신 상태인 event 목록 전송
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .forEach(entry -> sendToClient(sseEmitter, entry.getKey(), entry.getValue()));
        }

        return sseEmitter;
    }

    public void send(User receiver, NotificationType notificationType, String message, String content, Club club) {

        Notification notification = notificationRepository.save(
                Notification.builder()
                        .type(notificationType)
                        .receiver(receiver)
                        .isCheck(false)
                        .club(club)
                        .message(new NotificationMessage(message))
                        .content(content)
                        .title(new NotificationTitle(setTitle(notificationType,club)))
                        .build()
        );
        String userId = String.valueOf(receiver.getId());
        // 로그인한 유저의 sseEmitter 전체 호출
        Map<String,SseEmitter> sseEmitters = emitterRepository.findAllEmitterStartWithByUserId(userId);
        sseEmitters.forEach(
                (key,emitter)->{
                    log.info("key, notificationId : {}, {}", key, notification.getId());
                    emitterRepository.saveEventCache(key,notification);
                    NotificationResponse response = notification.toNotificationResponse();
                    ObjectMapper objectMapper = new ObjectMapper();
                    String jsonData = null;
                    try {
                        jsonData = objectMapper.writeValueAsString(response);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    sendToClient(emitter,key,jsonData);
                }
        );
    }

    private void sendToClient(SseEmitter sseEmitter, String emitterId, Object data) {
        try{
            sseEmitter.send(SseEmitter.event()
                    .id(emitterId)
                    .data(data));
        } catch (IOException e) {
            emitterRepository.deleteById(emitterId);
            throw new RuntimeException("SSE Connection Failed : 알림 전송 실패");
        }
    }

    public String setTitle(NotificationType notificationType,Club club){
        if(notificationType.equals(NotificationType.DOC_RESULT))
            return club.getName()+" "+notificationType.getKey();
        else if(notificationType.equals(NotificationType.FINAL_RESULT_FAIL)||notificationType.equals(NotificationType.FINAL_RESULT_PASS))
            return club.getName()+" 최종 발표";
        else
            return notificationType.getKey();
    }
}
