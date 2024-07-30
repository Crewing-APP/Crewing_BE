package com.crewing.push.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class FcmDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class FcmNotificationEvent {
        List<Long> userIds;
        private String title;
        private String body;
    }
}
