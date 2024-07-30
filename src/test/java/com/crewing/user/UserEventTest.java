package com.crewing.user;

import com.crewing.common.error.user.OverPointException;
import com.crewing.user.entity.PointEvent;
import com.crewing.user.entity.PointHistoryType;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.PointHistoryRepository;
import com.crewing.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class UserEventTest {

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PointHistoryRepository pointHistoryRepository;

    @Test
    @DisplayName("Save Point Event Test")
    @Transactional
    void savePointEventTest() {
        User test = User.builder()
                .email("test@naver.com")
                .name("test")
                .nickname("test")
                .password("test")
                .role(Role.USER)
                .build();

        User user = userRepository.save(test);

        int beforePoint = user.getPoint();

        PointEvent event = PointEvent.builder()
                .point(100)
                .type(PointHistoryType.SAVE)
                .userId(user.getId())
                .build();

        publisher.publishEvent(event);

        int afterPoint = user.getPoint();

        Assertions.assertEquals(beforePoint + 100, afterPoint);
    }

    @Test
    @DisplayName("Use Point Event Test")
    @Transactional
    void usePointEventTest() {
        User test = User.builder()
                .email("test@naver.com")
                .name("test")
                .nickname("test")
                .password("test")
                .role(Role.USER)
                .build();

        User user = userRepository.save(test);

        int beforePoint = user.getPoint();

        PointEvent event = PointEvent.builder()
                .point(-20)
                .type(PointHistoryType.USE)
                .userId(user.getId())
                .build();

        publisher.publishEvent(event);

        int afterPoint = user.getPoint();

        Assertions.assertEquals(beforePoint - 20, afterPoint);
    }

    @Test
    @DisplayName("Point Event Over Point Exception Test")
    @Transactional
    void pointEventOverPointExceptionTest() {
        User test = User.builder()
                .email("test@naver.com")
                .name("test")
                .nickname("test")
                .password("test")
                .role(Role.USER)
                .build();

        User user = userRepository.save(test);

        int beforePoint = user.getPoint();

        PointEvent event = PointEvent.builder()
                .point(-2000000)
                .type(PointHistoryType.USE)
                .userId(user.getId())
                .build();

        Assertions.assertThrows(OverPointException.class,
                () -> publisher.publishEvent(event));
    }
}
