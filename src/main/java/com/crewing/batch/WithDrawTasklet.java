package com.crewing.batch;

import com.crewing.applicant.repository.ApplicantRepository;
import com.crewing.device.repository.DeviceRepository;
import com.crewing.member.repository.MemberRepository;
import com.crewing.notification.repository.NotificationRepository;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.entity.User;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class WithDrawTasklet implements Tasklet {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicantRepository applicantRepository;
    private final ReviewRepository reviewRepository;

    @Override
    @Transactional
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        log.info("Start WithDraw TIME : {}", LocalDateTime.now());
        List<User> users = userRepository.findAllByDeleteAtBeforeTime(LocalDate.now().minusYears(1));
        List<Long> ids = users.stream().map(User::getId).toList();

        memberRepository.deleteAllByUserIdsIn(ids);
        notificationRepository.deleteAllByReceiverIdsIn(ids);
        applicantRepository.deleteAllByUserIdsIn(ids);

        deviceRepository.updateDeviceUserToNullByUserIdsIn(ids);
        interestRepository.updateInterestUserToNullByUserIdsIn(ids);
        reviewRepository.updateReviewUserToNullByUserIdsIn(ids);

        userRepository.deleteAllIdsIn(ids);

        ////
//        users.forEach(user -> {
//            memberRepository.deleteAllByUserId(user.getId());
//            notificationRepository.deleteAllByReceiverId(user.getId());
//            applicantRepository.deleteAllByUserId(user.getId());
//
//            List<Device> devices = deviceRepository.findAllByUserId(user.getId());
//            devices.forEach(device -> {
//                device.updateUser(null);
//                deviceRepository.save(device);
//            });
//
//            List<Interest> interests = interestRepository.findAllByUserId(user.getId());
//            interests.forEach(interest -> {
//                interest.setUser(null);
//                interestRepository.save(interest);
//            });
//
//            List<Review> reviews = reviewRepository.findAllByUserId(user.getId());
//            reviews.forEach(review -> {
//                review.deleteUser();
//                reviewRepository.save(review);
//            });
//            userRepository.delete(user);
//        });

        ////
//        for (int i = 0; i < 1000; i++) {
//            User user = User.builder()
//                    .role(Role.GUEST)
//                    .name("ads")
//                    .email("asd" + i)
//                    .nickname("qqqq" + i)
//                    .build();
//            user.delete();
//
//            Interest interest = Interest.builder()
//                    .build();
//            user.addInterest(interest);
//
//            User save = userRepository.save(user);
//            interestRepository.save(interest);
//
//            Review review = Review.builder()
//                    .user(save)
//                    .rate(1)
//                    .review("asd")
//                    .build();
//
//            Member member = Member.builder()
//                    .user(save)
//                    .build();
//
//            Device device = Device.builder()
//                    .user(save)
//                    .build();
//
//            Applicant applicant = Applicant.builder().user(save)
//                    .status(Status.WAIT)
//                    .build();
//
//            Notification notification = Notification.builder()
//                    .receiver(save)
//                    .isCheck(true)
//                    .message(new NotificationMessage("asd"))
//                    .title(new NotificationTitle("asd"))
//                    .build();
//
//            reviewRepository.save(review);
//            memberRepository.save(member);
//            deviceRepository.save(device);
//            applicantRepository.save(applicant);
//            notificationRepository.save(notification);
//        }
        return RepeatStatus.FINISHED;
    }
}
