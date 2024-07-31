package com.crewing.review;

import com.crewing.auth.service.AuthService;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.common.error.review.ReviewAccessDeniedException;
import com.crewing.common.error.review.ReviewAlreadyExistsException;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
import com.crewing.member.repository.MemberRepository;
import com.crewing.review.dto.ReviewCreateRequest;
import com.crewing.review.dto.ReviewListResponse;
import com.crewing.review.dto.ReviewResponse;
import com.crewing.review.dto.ReviewUpdateRequest;
import com.crewing.review.entity.Review;
import com.crewing.review.repository.ReviewAccessRepository;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.review.service.ReviewService;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class ReviewServiceTest {
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    ReviewAccessRepository reviewAccessRepository;
    @Autowired
    private EntityManager em;

    private User user;
    private Club club;

    @BeforeEach
    void setting(){
        User user = userRepository.save(User.builder()
                .name("test_standard")
                .role(com.crewing.user.entity.Role.ADMIN)
                .birth("2000")
                .gender("여자")
                .build());
        this.user = user;
        Club newClub = Club.builder()
                .name("test")
                .application("test")
                .category(0)
                .introduction("test")
                .oneLiner("test")
                .status(Status.HOLD)
                .isRecruit(true)
                .build();
        this.club = clubRepository.save(newClub);
        Member member = Member.builder()
                .club(club)
                .role(Role.MANAGER)
                .user(this.user)
                .build();
        memberRepository.save(member);
        clear();
    }

    private void clear(){
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("리뷰 생성 성공 테스트")
    void createReviewSuccessTest(){
        ReviewResponse result = createReview();
        assertThat(reviewRepository.findById(result.getReviewId())).isPresent();
    }

    @Test
    @DisplayName("리뷰 수정 성공 테스트")
    void updateReviewSuccessTest(){
        ReviewResponse review = createReview();
        ReviewUpdateRequest request = ReviewUpdateRequest.builder()
                .review("update")
                .rate(1)
                .build();
        ReviewResponse result = reviewService.updateReview(request, review.getReviewId(), user);

        assertThat(result.getReview()).isEqualTo("update");
        assertThat(result.getRate()).isEqualTo(1);
    }

    @Test
    @DisplayName("리뷰 전체 조회 테스트")
    void getAllReviewInfoSuccessTest(){
        Pageable pageRequest = PageRequest.of(0, 10);
        User user = userRepository.save(User.builder()
                .name("test1")
                .role(com.crewing.user.entity.Role.USER)
                .build());
        memberRepository.save(Member.builder()
                .role(com.crewing.member.entity.Role.MEMBER)
                .club(club)
                .user(user)
                .build());
        reviewRepository.save(Review.builder()
                .club(club)
                .user(user)
                .rate(5)
                .build());
        ReviewListResponse result = reviewService.getAllReviewInfo(user, pageRequest, club.getClubId());

        assertThat(reviewRepository.findAllByClub(pageRequest,club).getTotalElements()).isEqualTo(result.getTotalCnt());
        assertThat(result.getFive()).isEqualTo(1);

    }

    @Test
    @DisplayName("리뷰 삭제 성공 테스트")
    void deleteReviewSuccessTest(){
        ReviewResponse result = createReview();
        reviewService.deleteReview(result.getReviewId(), this.user);

        assertThat(reviewRepository.findById(result.getReviewId())).isEmpty();
    }

    private ReviewResponse createReview(){
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .review("review")
                .rate(5)
                .clubId(club.getClubId())
                .build();
        return reviewService.createReview(request, user);
    }

    @Test
    @DisplayName("리뷰 권한 추가 성공 테스트")
    void addReviewAccessSuccessTest(){
        User user = userRepository.save(User.builder()
                .name("test1")
                .role(com.crewing.user.entity.Role.USER)
                .build());
        reviewService.addReviewAccess(user,club.getClubId());

        assertThat(reviewAccessRepository.existsByUserAndClub(user,club)).isTrue();
    }

    @Test
    @DisplayName("리뷰 생성 실패 테스트 : ReviewAccessDeniedException")
    void createReviewReviewAccessDeniedExceptionTest(){
        User user = userRepository.save(User.builder()
                .name("test1")
                .role(com.crewing.user.entity.Role.USER)
                .build());
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .review("review")
                .rate(5)
                .clubId(club.getClubId())
                .build();
        assertThatThrownBy(()->reviewService.createReview(request,user)).isInstanceOf(ReviewAccessDeniedException.class);
    }

    @Test
    @DisplayName("리뷰 생성 실패 테스트 : ReviewAlreadyExistsException")
    void createReviewReviewAlreadyExistsExceptionTest(){
        ReviewResponse result = createReview();
        ReviewCreateRequest request = ReviewCreateRequest.builder()
                .review("review")
                .rate(5)
                .clubId(club.getClubId())
                .build();

        assertThatThrownBy(()->reviewService.createReview(request,user)).isInstanceOf(ReviewAlreadyExistsException.class);
    }
}
