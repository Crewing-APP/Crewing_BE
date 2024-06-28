package com.crewing.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    APPLY("결과 발표"),
    CLUB_ACCEPT("동아리 승인"),
    CLUB_RETURN("동아리 반려");

    private final String key;
}
