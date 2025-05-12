package org.govwatcher.batch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.govwatcher.batch.service.ParliamentStatBatchService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ParliamentStatTasklet implements Tasklet {

    private final ParliamentStatBatchService batchService;

    @Override
    @Transactional
    public RepeatStatus execute(
            @NonNull StepContribution contribution,
            @NonNull ChunkContext chunkContext) {
        batchService.aggregateAndSaveParliamentStat();
        return RepeatStatus.FINISHED;
    }
}