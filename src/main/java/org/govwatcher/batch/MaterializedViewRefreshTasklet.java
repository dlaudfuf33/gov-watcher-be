package org.govwatcher.batch;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.govwatcher.service.MaterializedViewService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaterializedViewRefreshTasklet implements Tasklet {

    private final MaterializedViewService materializedViewService;

    @Override
    public RepeatStatus execute(
            @NonNull StepContribution contribution,
            @NonNull ChunkContext chunkContext) {
        materializedViewService.refreshLegislativeNoticeMat();
        materializedViewService.refreshLegislativeNoticeDetailMat();
        materializedViewService.refreshPoliticianMat();
        materializedViewService.refreshPoliticianDetailMat();
        return RepeatStatus.FINISHED;
    }

}
