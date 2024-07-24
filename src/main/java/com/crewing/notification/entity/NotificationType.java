package com.crewing.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
    DOC_RESULT("서류 발표"),
    FINAL_RESULT_FAIL("최종 발표 탈락"),
    FINAL_RESULT_PASS("최종 발표 합격"),
    CLUB_ACCEPT("동아리 승인"),
    CLUB_RETURN("동아리 반려");

    private final String key;
}
