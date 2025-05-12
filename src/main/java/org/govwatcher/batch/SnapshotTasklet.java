package org.govwatcher.batch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.govwatcher.batch.service.ParliamentStatSnapshotService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SnapshotTasklet implements Tasklet {
    private final ParliamentStatSnapshotService parliamentStatSnapshotService;


    @Override
    public RepeatStatus execute(
            @NonNull StepContribution contribution,
            @NonNull ChunkContext chunkContext) throws Exception {
        parliamentStatSnapshotService.snapshotTodayStats();
        return RepeatStatus.FINISHED;
    }
}
