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
public class RemoveDependencyUserTasklet implements Tasklet {
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
        List<User> users = userRepository.findAllByDeleteAtBeforeTime(LocalDate.now().minusYears(1));
        List<Long> ids = users.stream().map(User::getId).toList();

        memberRepository.deleteAllByUserIdsIn(ids);
        notificationRepository.deleteAllByReceiverIdsIn(ids);
        applicantRepository.deleteAllByUserIdsIn(ids);

        deviceRepository.updateDeviceUserToNullByUserIdsIn(ids);
        interestRepository.updateInterestUserToNullByUserIdsIn(ids);
        reviewRepository.updateReviewUserToNullByUserIdsIn(ids);
        return RepeatStatus.FINISHED;
    }
}
