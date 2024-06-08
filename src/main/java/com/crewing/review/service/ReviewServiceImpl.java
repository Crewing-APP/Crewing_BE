package com.crewing.review.service;

import com.crewing.club.entity.Club;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.EntityNotFoundException;
import com.crewing.common.error.ErrorCode;
import com.crewing.common.error.club.ClubNotFoundException;
import com.crewing.common.error.review.ReviewAccessDeniedException;
import com.crewing.common.error.review.ReviewAlreadyExistsException;
import com.crewing.common.error.review.ReviewNotFoundException;
import com.crewing.member.repository.MemberRepository;
import com.crewing.review.dto.ReviewCreateRequest;
import com.crewing.review.dto.ReviewListResponse;
import com.crewing.review.dto.ReviewResponse;
import com.crewing.review.entity.Review;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ReviewResponse createReview(ReviewCreateRequest createRequest, User user) {
        Club club = clubRepository.findById(createRequest.getClubId()).orElseThrow(ClubNotFoundException::new);
        // 동아리 회원이 아니면 예외처리
        memberRepository.findByClubAndUser(club,user).orElseThrow(ReviewAccessDeniedException::new);
        Optional<Review> check = reviewRepository.findByClubAndUser(club,user);
        if(check.isPresent()){ // 이미 동아리에 대한 리뷰를 썼다면 더 이상 쓰지 못함.
            throw new ReviewAlreadyExistsException();
        }

        Review review = reviewRepository.save(Review.builder()
                .review(createRequest.getReview())
                .user(user)
                .club(club)
                .rate(createRequest.getRate())
                .build());
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

    @Override
    @Transactional
    public void deleteReview(Long reviewId, User user) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
        if(review.getUser().getId() != user.getId()) {
            throw new ReviewAccessDeniedException();
        }
        else{
            reviewRepository.delete(review);
        }
    }

    @Override
    @Transactional
    public ReviewListResponse getAllReviewInfo(Pageable pageable, Long clubId) {
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC,"reviewId"));
        Page<Review> reviewList = reviewRepository.findAllByClub(pageRequest, club);

        Page<ReviewResponse> reviewPages = reviewList.map(review -> ReviewResponse.builder()
                .clubId(review.getClub().getClubId())
                .reviewId(review.getReviewId())
                .review(review.getReview())
                .rate(review.getRate())
                .createdDate(review.getCreatedDate())
                .lastModifiedDate(review.getLastModifiedDate())
                .user(ReviewResponse.UserInfo.builder()
                        .userId(review.getUser().getId())
                        .nickname(review.getUser().getNickname())
                        .build())
                .build());

        return ReviewListResponse.builder()
                .reviews(reviewPages.getContent())
                .reviewAvg(reviewRepository.findAverageRateByClubId(club).orElse(0f))
                .pageNum(reviewList.getNumber())
                .pageSize(reviewList.getSize())
                .one(reviewRepository.findAllByClubAndRate(club,1).size())
                .two(reviewRepository.findAllByClubAndRate(club,2).size())
                .three(reviewRepository.findAllByClubAndRate(club,3).size())
                .four(reviewRepository.findAllByClubAndRate(club,4).size())
                .five(reviewRepository.findAllByClubAndRate(club,5).size())
                .clubId(clubId)
                .totalCnt(reviewList.getTotalElements())
                .build();
    }
}
