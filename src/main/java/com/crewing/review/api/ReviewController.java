package com.crewing.review.api;

import com.crewing.auth.entity.PrincipalDetails;
import com.crewing.review.dto.ReviewCreateRequest;
import com.crewing.review.dto.ReviewListResponse;
import com.crewing.review.dto.ReviewResponse;
import com.crewing.review.dto.ReviewUpdateRequest;
import com.crewing.review.entity.Review;
import com.crewing.review.service.ReviewService;
import com.crewing.review.service.ReviewServiceImpl;
import com.crewing.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "review", description = "리뷰 API")
@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/review")
public class ReviewController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 생성", description = "특정 동아리에 대한 리뷰 생성, 작성 시 포인트 부여(최초 작성 : 20포인트, 일반 : 10포인트)")
    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> create(@RequestBody ReviewCreateRequest createRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ReviewResponse reviewResponse = reviewService.createReview(createRequest,principalDetails.getUser());
        return ResponseEntity.ok().body(reviewResponse);
    }

    @Operation(summary = "리뷰 삭제", description = "특정 동아리에 대한 리뷰 삭제, 작성자만 가능, 삭제 시 포인트 회수(최초 작성 : 20포인트, 일반 : 10포인트)")
    @DeleteMapping("/delete/{reviewId}")
    @Parameter(name = "reviewId", description = "리뷰 아이디", required = true)
    public ResponseEntity<String> delete(@PathVariable Long reviewId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        reviewService.deleteReview(reviewId,principalDetails.getUser());
        return ResponseEntity.ok().body("Delete successful");
    }

    @Operation(summary = "리뷰 수정", description = "리뷰 수정, 작성자만 가능")
    @PatchMapping("/{reviewId}")
    @Parameter(name = "reviewId", description = "리뷰 아이디", required = true)
    public ResponseEntity<ReviewResponse> update(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequest updateRequest, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ReviewResponse response = reviewService.updateReview(updateRequest,reviewId,principalDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "리뷰 전체 조회", description = "특정 동아리에 대한 모든 리뷰 조회, 회원 또는 포인트로 구매한 회원만 열람 가능")
    @GetMapping("/reviews/{clubId}")
    @Parameter(name = "clubId", description = "동아리 아이디", required = true)
    public ResponseEntity<ReviewListResponse> getAllReview(@PageableDefault(size = 10) Pageable pageable, @PathVariable Long clubId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        ReviewListResponse reviewResponseList = reviewService.getAllReviewInfo(principalDetails.getUser(),pageable,clubId);
        return ResponseEntity.ok().body(reviewResponseList);
    }

    @Operation(summary = "리뷰 열람 권한 포인트 구매", description = "5포인트 차감하여 리뷰 열람 권한 획득")
    @PostMapping("/reviews/{clubId}/purchase")
    @Parameter(name = "clubId", description = "동아리 아이디", required = true)
    public ResponseEntity<String> purchaseReviewAccess(@PathVariable Long clubId, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        reviewService.addReviewAccess(principalDetails.getUser(),clubId);
        return ResponseEntity.ok().body("Purchase successful");
    }
}
