package com.crewing.review.service;

import com.crewing.review.dto.ReviewCreateRequest;
import com.crewing.review.dto.ReviewListResponse;
import com.crewing.review.dto.ReviewResponse;
import com.crewing.review.dto.ReviewUpdateRequest;
import com.crewing.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewResponse createReview(ReviewCreateRequest createRequest, User user);
    void deleteReview(Long reviewId, User user);
    ReviewResponse updateReview(ReviewUpdateRequest request, Long reviewId, User user);
    ReviewListResponse getAllReviewInfo(Pageable pageable, Long clubId);
}
