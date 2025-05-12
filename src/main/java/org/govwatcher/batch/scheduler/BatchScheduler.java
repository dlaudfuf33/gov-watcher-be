package org.govwatcher.batch.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {

    private final JobLauncher jobLauncher;

    @Qualifier("parliamentStatJob")
    private final Job parliamentStatJob;

    // 매일 새벽 2시에 실행
    @Scheduled(cron = "0 0 * * * ?")
    public void runParliamentStatJob() throws Exception {
        jobLauncher.run(parliamentStatJob, new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters());

        log.info("[Batch] parliamentStatJob executed at {}", LocalDateTime.now());
    }
}