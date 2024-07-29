package com.crewing.user.service;

import com.crewing.common.error.BusinessException;
import com.crewing.common.error.ErrorCode;
import com.crewing.common.error.user.UserNotFoundException;
import com.crewing.external.StudentVerifyApi.StudentVerifyApiClient;
import com.crewing.file.service.FileService;
import com.crewing.user.dto.UserDTO.InterestUpdateRequest;
import com.crewing.user.dto.UserDTO.UserInfoResponse;
import com.crewing.user.entity.Interest;
import com.crewing.user.entity.PointEvent;
import com.crewing.user.entity.PointHistory;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.PointHistoryRepository;
import com.crewing.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
//    @Value("${student.verify.apiKey}")
//    private String apiKey;

    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final FileService fileService;
    private final StudentVerifyApiClient studentVerifyApiClient;
    private final PointHistoryRepository pointHistoryRepository;

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserInfoResponse.toDTO(user);
    }

    @Transactional
    public void updateInterest(Long userId, InterestUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        List<Interest> oldInterests = user.getInterests();
        oldInterests.forEach(interest -> interest.setUser(null));
        interestRepository.deleteAll(oldInterests);

        List<Interest> interests = new ArrayList<>();

        request.getInterests().forEach(interest ->
                interests.add(Interest.builder().interest(interest.getInterest()).build()));

        interestRepository.saveAll(interests);

        user.updateInterests(interests);

        userRepository.save(user);
    }

    @Transactional
    public void updateUserNickname(Long userId, String nickname) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        user.updateNickname(nickname);

        userRepository.save(user);
    }

    @Transactional
    public void updateUserProfileImage(Long userId, MultipartFile image) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        String profileImage;

        try {
            profileImage = fileService.uploadFile(image);
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        user.updateProfileImage(profileImage);

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        user.delete();
        userRepository.save(user);
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
    }


    /**
     * Point 갱신 이벤트 point + -> 포인트 적립(EARN) , - -> 포인트 사용(USE)
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void updatePoint(PointEvent event) {
        log.info("Point Event Start");
        User user = userRepository.findById(event.getUserId()).orElseThrow(UserNotFoundException::new);
        user.updatePoint(user.getPoint() + event.getPoint());
        User save = userRepository.save(user);

        PointHistory history = PointHistory.builder()
                .point(event.getPoint())
                .type(event.getType())
                .user(save)
                .build();

        pointHistoryRepository.save(history);
    }

//    public void createStudentEmailVerification(StudentCreateVerificationRequest request) {
//        studentVerifyApiClient.createStudentVerification(apiKey, request);
//    }
//
//    public void verifyEmail(Long userId, StudentVerifyRequest request) {
//        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
//
//        studentVerifyApiClient.verifyEmail(apiKey, request);
//
//        user.verifyStudent();
//
//        userRepository.save(user);
//    }

}
