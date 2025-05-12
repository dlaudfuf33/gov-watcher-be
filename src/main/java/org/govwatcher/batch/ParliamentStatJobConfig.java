package org.govwatcher.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class ParliamentStatJobConfig {
    private final JobRepository jobRepository;

    // 트랜잭션 매니저 (Step 생성시 필요)
    private final PlatformTransactionManager transactionManager;
    private final SnapshotTasklet snapshotTasklet;
    private final ParliamentStatTasklet parliamentStatTasklet;
    private final MaterializedViewRefreshTasklet materializedViewRefreshTasklet;

    // Job 정의
    @Bean
    public Job parliamentStatJob() {
        return new JobBuilder("parliamentStatJob", jobRepository).start(parliamentStatStep()).next(snapshotStep()).next(refreshMaterializedViewStep()).build();
    }

    // Step 정의
    @Bean
    public Step snapshotStep() {
        return new StepBuilder("snapshotStep", jobRepository).tasklet(snapshotTasklet, transactionManager).build();
    }

    @Bean
    public Step parliamentStatStep() {
        return new StepBuilder("parliamentStatStep", jobRepository).tasklet(parliamentStatTasklet, transactionManager).build();
    }

    @Bean
    public Step refreshMaterializedViewStep() {
        return new StepBuilder("refreshMaterializedViewStep", jobRepository).tasklet(materializedViewRefreshTasklet, transactionManager).build();

    }
}
