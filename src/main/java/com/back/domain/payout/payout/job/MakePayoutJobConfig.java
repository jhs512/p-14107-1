package com.back.domain.payout.payout.job;

import com.back.domain.payout.payout.service.PayoutService;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class MakePayoutJobConfig {
    private final MakePayoutJobConfig self;
    private final PayoutService payoutService;

    public MakePayoutJobConfig(
            @Lazy MakePayoutJobConfig self,
            PayoutService payoutService
    ) {
        this.self = self;
        this.payoutService = payoutService;
    }

    @Bean
    public Job makePayoutJob(
            JobRepository jobRepository,
            Step makePayoutStep
    ) {
        return new JobBuilder("makePayoutJob", jobRepository)
                .start(makePayoutStep)
                .build();
    }

    @Bean
    public Step makePayoutStep(JobRepository jobRepository) {
        return new StepBuilder("makePayoutStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    while (true) {
                        int makeItemCount = self.makeDuePayoutsMore(1);

                        if (makeItemCount == 0) break;
                    }

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Transactional
    public int makeDuePayoutsMore(int limit) {
        return payoutService.makeDuePayouts(limit).data();
    }
}
