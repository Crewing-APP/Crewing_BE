package com.crewing.review;

import com.crewing.auth.dto.SignUpDTO;
import com.crewing.auth.service.AuthService;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.member.entity.Member;
import com.crewing.member.entity.Role;
import com.crewing.member.repository.MemberRepository;
import com.crewing.review.dto.ReviewCreateRequest;
import com.crewing.review.dto.ReviewResponse;
import com.crewing.review.entity.Review;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.review.service.ReviewServiceImpl;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS) // 클래스 단위로 생명주기를 바꾸면 static을 붙일 필요가 없어짐
public class ReviewServiceImplTest {
    @Autowired
    private ReviewServiceImpl reviewService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    private User user;
    private Club club;
    private Review review;

    @BeforeAll
    void setting(){
        reviewRepository.deleteAll();
        memberRepository.deleteAll();
        clubRepository.deleteAll();
        userRepository.deleteAll();
        authService.getDevToken("tlsdmsgp33@naver.com");
        this.user = userRepository.findByEmail("tlsdmsgp33@naver.com").get();
        Club newClub = Club.builder()
                .name("test")
                .application("test")
                .category(0)
                .introduction("test")
                .fileList(null)
                .oneLiner("test")
                .profile("test")
                .status(Status.UNDEFINED)
                .build();
        this.club = clubRepository.save(newClub);
        Member member = Member.builder()
                .club(club)
                .role(Role.MANAGER)
                .user(this.user)
                .build();
        memberRepository.save(member);
        this.review = Review.builder()
                .rate(5)
                .club(this.club)
                .review("test")
                .user(this.user)
                .build();
        reviewRepository.save(review);
    }

    @Test
    @DisplayName("리뷰 생성 테스트")
    void createReview(){
        ReviewCreateRequest reviewCreateRequest = ReviewCreateRequest.builder()
                .clubId(this.club.getClubId())
                .rate(5)
                .review("test")
                .build();
        ReviewResponse review = reviewService.createReview(reviewCreateRequest, user);
        Assertions.assertThat(review.getReview()).isEqualTo("test");
        Assertions.assertThat(review.getUser().getUserId()).isEqualTo(user.getId());
        List<Review> reviews = reviewRepository.findAll();
        System.out.println(reviews.size());
    }

    @Test
    @DisplayName("리뷰 평균 구하기 테스트")
    void getReviewAvg(){
        ReviewCreateRequest reviewCreateRequest = ReviewCreateRequest.builder()
                .clubId(this.club.getClubId())
                .rate(5)
                .review("test")
                .build();
        ReviewResponse review = reviewService.createReview(reviewCreateRequest, user);
        float avg = reviewRepository.findAverageRateByClubId(this.club);
        Assertions.assertThat(avg).isEqualTo(5.0f);
    }

//    @Test
//    @DisplayName("리뷰 전체 조회 테스트")
//    void getAllReviewInfo(){
//        List<Review> reviews = reviewRepository.findAll();
//        System.out.println(reviews.size());
//
//
//        List<ReviewResponse> reviewList = reviewService.getAllReviewInfo(this.club.getClubId());
//        Assertions.assertThat(reviewList.size()).isEqualTo(1);
//        Assertions.assertThat(reviewList.get(0).getReview()).isEqualTo("test");
//    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void deleteReview(){
        reviewService.deleteReview(this.review.getReviewId(), this.user);
        Optional<Review> review = reviewRepository.findById(this.review.getReviewId());
        Assertions.assertThat(review.isPresent()).isFalse();
    }

//    @AfterAll
//    void cleanUp(){
//        reviewRepository.deleteAll();
//        memberRepository.deleteAll();
//        clubRepository.deleteAll();
//        userRepository.deleteAll();
//    }
}
