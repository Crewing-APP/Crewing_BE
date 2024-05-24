package com.crewing.review.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponse {
    private Long reviewId;
    private Long clubId;
    private String review;
    private UserInfo user;
    private int rate;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    @Getter
    @Builder
    public static class UserInfo{
        private Long userId;
        private String nickname;
    }
}
