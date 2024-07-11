package com.crewing.club;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.club.service.ClubReadService;
import com.crewing.club.service.ClubService;
import com.crewing.member.entity.Member;
import com.crewing.member.repository.MemberRepository;
import com.crewing.review.entity.Review;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.entity.Interest;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
public class ClubServiceImplTest {
    @Autowired
    private ClubService clubService;
    @Autowired
    private ClubReadService clubReadService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private EntityManager em;

    User user=null;
    Club club=null;
    @BeforeEach
    void init(){
        User user1 = userRepository.save(User.builder()
                .name("test_standard")
                .role(Role.USER)
                .birth("2000")
                .gender("여자")
                .build());
        this.user = user1;
        User user2 = userRepository.save(User.builder()
                .name("test1")
                .role(Role.USER)
                .birth("2005")
                .gender("남자")
                .build());
        User user3 = userRepository.save(User.builder()
                .name("test2")
                .role(Role.USER)
                .birth("2000")
                .gender("남자")
                .build());
        User user4 = userRepository.save(User.builder()
                .name("test3")
                .role(Role.USER)
                .birth("2001")
                .gender("여자")
                .build());
        List<Interest> interests = new ArrayList<>();
        interests.add(interestRepository.save(Interest.builder()
                        .interest("IT/데이터")
                        .user(user1)
                        .build()));
        this.user.updateInterests(interests);
        // Club 생성 (추천 순서대로 생성)
        Club club1 = clubRepository.save(Club.builder()
                .name("test1")
                .isRecruit(true)
                .status(Status.ACCEPT)
                .category(0)
                .build());
        this.club = club1;
        Club club2 = clubRepository.save(Club.builder()
                .name("test2")
                .isRecruit(true)
                .status(Status.ACCEPT)
                .category(0)
                .build());
        Club club3 = clubRepository.save(Club.builder()
                .name("test3")
                .isRecruit(true)
                .status(Status.ACCEPT)
                .category(0)
                .build());
        // member 생성
        memberRepository.save(Member.builder()
                .role(com.crewing.member.entity.Role.MEMBER)
                .club(club1)
                .user(user2)
                .build());
        memberRepository.save(Member.builder()
                .role(com.crewing.member.entity.Role.MEMBER)
                .club(club2)
                .user(user3)
                .build());
        memberRepository.save(Member.builder()
                .role(com.crewing.member.entity.Role.MEMBER)
                .club(club3)
                .user(user4)
                .build());
        // review 생성
        reviewRepository.save(Review.builder()
                .club(club1)
                .user(user2)
                .rate(5)
                .build());
        clear();
    }

    private void clear(){
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("추천 동아리 리스트 테스트")
    void getAllRecommendedClubInfo(){
        Pageable pageable = PageRequest.of(0, 10);
        ClubListResponse clubListResponse = clubReadService.getAllRecommendedClubInfo(pageable, "",user);
        List<ClubListInfoResponse> clubLists = clubListResponse.getClubs();

        assertThat(clubLists.get(0).getName()).isEqualTo("test1");
        assertThat(clubLists.get(0).getIsRecruit().equals(true));
        assertThat(clubLists.get(1).getName()).isEqualTo("test2");
        assertThat(clubLists.get(2).getName()).isEqualTo("test3");
        for(ClubListInfoResponse clubInfoResponse : clubLists){
            System.out.println("=========================================");
            System.out.println(clubInfoResponse.getName());
            System.out.println(clubInfoResponse.getReviewAvg());
            System.out.println(clubInfoResponse.getReviewNum());
        }
    }
}
