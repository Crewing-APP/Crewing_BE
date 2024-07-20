package com.crewing.review.dto;

import com.crewing.review.entity.Review;
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

    public static ReviewResponse toReviewResponse(Review review) {
        ReviewResponse.UserInfo userInfo = ReviewResponse.UserInfo.builder()
                .userId(review.getUser().getId())
                .nickname(review.getUser().getNickname())
                .build();

        return ReviewResponse.builder()
                .clubId(review.getClub().getClubId())
                .reviewId(review.getReviewId())
                .review(review.getReview())
                .rate(review.getRate())
                .createdDate(review.getCreatedDate())
                .lastModifiedDate(review.getLastModifiedDate())
                .user(userInfo)
                .build();
    }
}
