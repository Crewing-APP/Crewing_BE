package com.crewing.push.api;

import com.crewing.push.dto.FcmDto.FcmNotificationEvent;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "유저", description = "유저 정보를 관리 합니다")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/fcm")
public class FcmController {
    private final ApplicationEventPublisher publisher;

    @GetMapping
    public ResponseEntity<Void> test() {
        List<Long> test = new ArrayList<>();
        test.add(1L);

        FcmNotificationEvent event = FcmNotificationEvent.builder()
                .userIds(test)
                .title("test")
                .body("tset")
                .build();

        publisher.publishEvent(event);

        return ResponseEntity.ok().build();
    }
}
