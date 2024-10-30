package com.crewing.club;

import com.crewing.club.dto.*;
import com.crewing.club.entity.Status;
import com.crewing.club.repository.ClubRepository;
import com.crewing.club.service.ClubService;
import com.crewing.common.error.club.ClubAccessDeniedException;
import com.crewing.notification.entity.Notification;
import com.crewing.notification.entity.NotificationType;
import com.crewing.notification.repository.NotificationRepository;
import com.crewing.user.entity.Role;
import com.crewing.user.entity.User;
import com.crewing.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
public class ClubServiceTest {
    @Autowired
    private ClubService clubService;
    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EntityManager em;

    User user=null;

    @BeforeEach
    void init(){
        User user = userRepository.save(User.builder()
                .name("test_standard")
                .role(Role.ADMIN)
                .birth("2000")
                .gender("여자")
                .build());
        this.user = user;

        clear();
    }

    private void clear(){
        em.flush();
        em.clear();
    }

    @Test
    @DisplayName("동아리 생성 성공 테스트")
    void createClubSuccessTest() throws IOException {
        ClubCreateResponse club = createClub();

        assertThat(clubRepository.findById(club.getClubId())).isNotNull();
    }

    @Test
    @DisplayName("동아리 수정 성공 테스트")
    void updateClubSuccessTest() throws IOException {
        ClubCreateResponse club = createClub();

        ClubUpdateRequest clubUpdateRequest = ClubUpdateRequest.builder()
                .category(club.getCategory())
                .application(club.getApplication())
                .name(club.getName())
                .isRecruit(true)
                .introduction(club.getIntroduction())
                .category(1)
                .oneLiner(club.getOneLiner())
                .isOnlyStudent(club.getIsOnlyStudent())
                .build();
        ClubCreateResponse result = clubService.updateClub(club.getClubId(),clubUpdateRequest,user,null,null,null);
        assertThat(result.getCategory()).isEqualTo(1);
    }

    @Test
    @DisplayName("동아리 삭제 성공 테스트")
    void deleteClubSuccessTest() throws IOException {
        ClubCreateResponse club = createClub();

        clubService.deleteClub(club.getClubId(),user);
        assertThat(clubRepository.findById(club.getClubId())).isEmpty();
    }

    @Test
    @DisplayName("동아리 상태 변경 성공 테스트")
    void changeStatusSuccessTest() throws IOException {
        ClubCreateResponse club = createClub();

        ClubChangeStatusRequest clubChangeStatusRequest = ClubChangeStatusRequest.builder()
                .clubId(club.getClubId())
                .status(Status.ACCEPT)
                .content("test")
                .build();

        ClubCreateResponse result = clubService.changeStatus(clubChangeStatusRequest,user);
        Notification notification = notificationRepository.findByReceiverAndClub(user,clubRepository.findById(club.getClubId()).get()).get();

        assertThat(result.getStatus()).isEqualTo(Status.ACCEPT);
        assertThat(notification.getContent()).isEqualTo("test");
        assertThat(notification.getType()).isEqualTo(NotificationType.CLUB_ACCEPT);

    }

    @Test
    @DisplayName("동아리 수정 실패 테스트 : ClubAccessDeniedException")
    void clubUpdateAccessDeniedExceptionTest() throws IOException {
        ClubCreateResponse club = createClub();
        ClubUpdateRequest clubUpdateRequest = ClubUpdateRequest.builder()
                .category(club.getCategory())
                .application(club.getApplication())
                .name(club.getName())
                .isRecruit(true)
                .introduction(club.getIntroduction())
                .category(1)
                .oneLiner(club.getOneLiner())
                .isOnlyStudent(club.getIsOnlyStudent())
                .build();
        User other = userRepository.save(User.builder()
                .name("other")
                .role(Role.USER)
                .build());

        assertThatThrownBy(() -> clubService.updateClub(club.getClubId(),clubUpdateRequest,other,null,null,null))
                .isInstanceOf(ClubAccessDeniedException.class);
    }

    @Test
    @DisplayName("동아리 삭제 실패 테스트 : ClubAccessDeniedException")
    void deleteClubAccessDeniedExceptionTest() throws IOException {
        ClubCreateResponse club = createClub();
        User other = userRepository.save(User.builder()
                .name("other")
                .role(Role.USER)
                .build());

        assertThatThrownBy(() -> clubService.deleteClub(club.getClubId(),other))
                .isInstanceOf(ClubAccessDeniedException.class);
    }

    @Test
    @DisplayName("동아리 상태 변경 실패 테스트 : ClubAccessDeniedException")
    void changeStatusAccessDeniedExceptionTest() throws IOException {
        ClubCreateResponse club = createClub();
        User other = userRepository.save(User.builder()
                .name("other")
                .role(Role.USER)
                .build());

        ClubChangeStatusRequest clubChangeStatusRequest = ClubChangeStatusRequest.builder()
                .clubId(club.getClubId())
                .status(Status.ACCEPT)
                .content("test")
                .build();
        assertThatThrownBy(() -> clubService.changeStatus(clubChangeStatusRequest,other))
                .isInstanceOf(ClubAccessDeniedException.class);
    }

    private ClubCreateResponse createClub() throws IOException {
        ClubCreateRequest clubCreateRequest = ClubCreateRequest.builder()
                .name("test")
                .application("test")
                .category(0)
                .isRecruit(true)
                .introduction("test")
                .oneLiner("test")
                .isOnlyStudent(false)
                .build();
        return clubService.createClub(clubCreateRequest,user,null,null);
    }

}
