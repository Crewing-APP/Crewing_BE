package com.crewing.notification.api;

import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.club.entity.Club;
import com.crewing.club.repository.ClubRepository;
import com.crewing.notification.dto.NotificationListResponse;
import com.crewing.notification.entity.NotificationType;
import com.crewing.notification.service.NotificationService;
import com.crewing.notification.service.NotificationServiceImpl;
import com.crewing.notification.service.SSEService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Tag(name = "notification", description = "알림 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {
    private final SSEService SSEService;
    private final NotificationService notificationServiceImpl;
    private final ClubRepository clubRepository;
    // Last-Event-ID는 sse 연결이 끊어졌을 때 클라이언트가 받은 마지막 메세지. 항상 존재 x
    @Operation(summary = "sse세션연결",description = "알림을 계속 받으려면 sse세션 연결을 먼저 실행해야함")
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(SSEService.subscribe(principalDetails.getUser(), lastEventId));
    }

    @Operation(summary = "사용자 알림 목록 조회",description = "로그인한 사용자의 모든 알림 조회, \n" +
            "알림 타입 : DOC_RESULT(서류 발표),FINAL_RESULT_FAIL(최종 발표 탈락),FINAL_RESULT_PASS(최종 발표 합격),CLUB_ACCEPT(동아리 승인),CLUB_RETURN(동아리 반려)\n"+
            "페이징 처리\n")
    @GetMapping("/notifications")
    public ResponseEntity<NotificationListResponse> getAllNotificationInfo(@PageableDefault(size = 10) Pageable pageable, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        NotificationListResponse notificationListResponse = notificationServiceImpl.getAllNotificationInfo(principalDetails.getUser(),pageable);
        return ResponseEntity.ok().body(notificationListResponse);
    }

    @Operation(summary = "알림 확인 체크",description = "알림 조회 클릭 시 확인 여부를 true로 변경")
    @PatchMapping("/check/{notificationId}")
    public ResponseEntity<String> checkNotification(@PathVariable("notificationId") Long notificationId){
        notificationServiceImpl.checkNotification(notificationId);
        return ResponseEntity.ok().body("Checking Successful");
    }

    @Operation(summary = "알림 삭제",description = "특정 알림 삭제")
    @DeleteMapping("/delete/{notificationId}")
    public ResponseEntity<String> deleteNotification(@PathVariable("notificationId") Long notificationId){
        notificationServiceImpl.deleteNotification(notificationId);
        return ResponseEntity.ok().body("Delete Successful");
    }
}
