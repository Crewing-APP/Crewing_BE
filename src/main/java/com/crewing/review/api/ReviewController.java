package com.crewing.review.api;

import com.crewing.review.dto.ReviewCreateRequest;
import com.crewing.review.dto.ReviewListResponse;
import com.crewing.review.dto.ReviewResponse;
import com.crewing.review.entity.Review;
import com.crewing.review.service.ReviewServiceImpl;
import com.crewing.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewServiceImpl reviewService;

    @Operation(summary = "리뷰 생성", description = "특정 동아리에 대한 리뷰 생성")
    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> create(@RequestBody ReviewCreateRequest createRequest, @AuthenticationPrincipal User user) {
        ReviewResponse reviewResponse = reviewService.createReview(createRequest,user);
        return ResponseEntity.ok().body(reviewResponse);
    }

    @Operation(summary = "리뷰 전체 조회", description = "특정 동아리에 대한 모든 리뷰 조회")
    @GetMapping("/reviews/{clubId}")
    public ResponseEntity<ReviewListResponse> getAllReview(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long clubId) {
        ReviewListResponse reviewResponseList = reviewService.getAllReviewInfo(pageable,clubId);
        return ResponseEntity.ok().body(reviewResponseList);
    }

    @Operation(summary = "리뷰 삭제", description = "특정 동아리에 대한 리뷰 삭제, 작성자만 가능")
    @DeleteMapping("/delete/{reviewId}")
    public ResponseEntity<String> delete(@PathVariable Long reviewId, @AuthenticationPrincipal User user) {
        reviewService.deleteReview(reviewId,user);
        return ResponseEntity.ok().body("Delete successful");
    }
}