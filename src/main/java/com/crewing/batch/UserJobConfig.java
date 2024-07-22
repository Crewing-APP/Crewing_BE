package com.crewing.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class UserJobConfig {

    private final WithDrawUserTasklet withDrawUserTasklet;

    @Bean
    public Job withDrawUserJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("withDrawUserJob", jobRepository)
                .start(withDrawUserStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step removeDependencyUserStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("withDrawUserStep", jobRepository)
                .tasklet(withDrawUserTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step withDrawUserStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("withDrawUserStep", jobRepository)
                .tasklet(withDrawUserTasklet, transactionManager)
                .build();
    }


}
