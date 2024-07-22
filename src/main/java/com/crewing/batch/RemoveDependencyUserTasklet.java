package com.crewing.batch;

import com.crewing.applicant.repository.ApplicantRepository;
import com.crewing.device.repository.DeviceRepository;
import com.crewing.member.repository.MemberRepository;
import com.crewing.notification.repository.NotificationRepository;
import com.crewing.review.repository.ReviewRepository;
import com.crewing.user.repository.InterestRepository;
import com.crewing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RemoveDependencyUserTasklet implements Tasklet {
    private final UserRepository userRepository;
    private final InterestRepository interestRepository;
    private final DeviceRepository deviceRepository;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final ApplicantRepository applicantRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        return RepeatStatus.FINISHED;
    }
}
