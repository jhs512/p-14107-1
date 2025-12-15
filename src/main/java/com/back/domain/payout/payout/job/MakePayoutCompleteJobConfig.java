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
public class MakePayoutCompleteJobConfig {
    private final MakePayoutCompleteJobConfig self;
    private final PayoutService payoutService;

    public MakePayoutCompleteJobConfig(
            @Lazy MakePayoutCompleteJobConfig self,
            PayoutService payoutService
    ) {
        this.self = self;
        this.payoutService = payoutService;
    }

    @Bean
    public Job makePayoutCompleteJob(
            JobRepository jobRepository,
            Step makePayoutCompleteStep
    ) {
        return new JobBuilder("makePayoutCompleteJob", jobRepository)
                .start(makePayoutCompleteStep)
                .build();
    }

    @Bean
    public Step makePayoutCompleteStep(JobRepository jobRepository) {
        return new StepBuilder("makePayoutCompleteStep", jobRepository)
                .tasklet((contribution, chunkContext) -> {
                    while (true) {
                        int completeItemCount = self.makeDuePayoutsCompleteMore(1);

                        if (completeItemCount == 0) break;
                    }

                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Transactional
    public int makeDuePayoutsCompleteMore(int limit) {
        return payoutService.makeDuePayoutsComplete(limit).data();
    }
}
