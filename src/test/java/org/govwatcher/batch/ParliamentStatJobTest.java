package org.govwatcher.batch;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ParliamentStatJobTest {

    // 배치 Job 실행기
    @Autowired
    private JobLauncher jobLauncher;

    // 국회 통계 집계 Job
    @Autowired
    private Job parliamentStatJob;

    @Test
    void parliamentStatJob_정상실행() throws Exception {
        // Job 실행
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis()) // 매번 다른 파라미터 (중복 방지)
                .toJobParameters();

        var jobExecution = jobLauncher.run(parliamentStatJob, jobParameters);

        // 실행 결과 검증
        assertThat(jobExecution.getExitStatus().getExitCode()).isEqualTo("COMPLETED");
    }
}