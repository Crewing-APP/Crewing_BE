package com.crewing.club;

import com.crewing.club.dto.ClubInfoResponse;
import com.crewing.club.dto.ClubListResponse;
import com.crewing.club.entity.Club;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.club.service.ClubReadService;
import com.crewing.common.error.user.UserAccessDeniedException;
import com.crewing.member.entity.Member;
import com.crewing.member.repository.MemberRepository;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class ClubReadServiceTest {
    @Autowired
    private ClubReadService clubReadService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EntityManager em;

    User user=null;
    Club club=null;

    @BeforeEach
    void init(){
        User user = userRepository.save(User.builder()
                .name("test_standard")
                .role(Role.ADMIN)
                .birth("2000")
                .gender("여자")
                .build());
        this.user = user;
        Club club1 = clubRepository.save(Club.builder()
                .name("test1")
                .isRecruit(true)
                .status(Status.ACCEPT)
                .category(0)
                .build());
        this.club = club1;
        memberRepository.save(Member.builder()
                .role(com.crewing.member.entity.Role.MEMBER)
                .club(club1)
                .user(user)
                .build());
        clear();
    }

    private void clear(){
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("동아리 상세 조회 성공 테스트")
    void getClubInfoSuccessTest(){
        ClubInfoResponse result = clubReadService.getClubInfo(club.getClubId(),user);

        assertThat(result.getName()).isEqualTo("test1");
        assertThat(result.getStatus()).isEqualTo(Status.ACCEPT);
        assertThat(result.getIsReviewAccess()).isTrue();
        assertThat(result.getCategory()).isEqualTo(0);
    }

    @Test
    @DisplayName("동아리 전체 조회 성공 테스트")
    void getAllClubInfoSuccessTest(){
        Pageable pageRequest = PageRequest.of(0, 10);
        ClubListResponse clubListResponse = clubReadService.getAllClubInfo(pageRequest);

        assertThat(clubListResponse.getTotalCnt().intValue()).isEqualTo(clubRepository.countAllByStatus(Status.ACCEPT));
    }

    @Test
    @DisplayName("동아리 카테고리별 조회 성공 테스트")
    void getAllFilterClubInfoSuccessTest(){
        Pageable pageRequest = PageRequest.of(0, 10);
        ClubListResponse clubListResponse = clubReadService.getAllFilterClubInfo(pageRequest,0);

        Long size = clubRepository.findAllByCategoryAndStatus(0,Status.ACCEPT,pageRequest).getTotalElements();
        assertThat(clubListResponse.getTotalCnt()).isEqualTo(size);
    }

    @Test
    @DisplayName("동아리 검색어 조회 성공 테스트")
    void getAllSearchClubInfoSuccessTest(){
        Club club = clubRepository.save(Club.builder()
                .isRecruit(true)
                .name("Club For Success Test0")
                .status(Status.ACCEPT)
                .category(0)
                .build());

        Pageable pageRequest = PageRequest.of(0, 10);
        ClubListResponse clubListResponse = clubReadService.getAllSearchClubInfo(pageRequest,"ClubForSuccessTest0",0);

        Long size = clubRepository.findAllByKeywordAndStatus("ClubForSuccessTest0",Status.ACCEPT,pageRequest).getTotalElements();
        assertThat(clubListResponse.getTotalCnt()).isEqualTo(size);
    }

    @Test
    @DisplayName("나의 동아리 조회 성공 테스트")
    void getAllMyClubInfoSuccessTest(){
        Pageable pageRequest = PageRequest.of(0, 10);
        ClubListResponse result = clubReadService.getAllMyClubInfo(pageRequest, user);

        assertThat(result.getTotalCnt()).isEqualTo(1L);
    }

    @Test
    @DisplayName("모든 동아리 상태 조회 실패 테스트 : UserAccessDeniedException")
    void getAllStatusClubInfoUserAccessDeniedExceptionTest(){
        Pageable pageRequest = PageRequest.of(0, 10);
        User user = userRepository.save(User.builder()
                .name("test1")
                .role(Role.USER)
                .build());

        assertThatThrownBy(()->clubReadService.getAllStatusClubInfo(pageRequest, Status.ACCEPT, user))
                .isInstanceOf(UserAccessDeniedException.class);
    }

}
