package org.govwatcher.dto.dashboard;

import lombok.Builder;
import lombok.Data;
import org.govwatcher.batch.model.ParliamentStat;
import org.govwatcher.batch.model.ParliamentStatHistory;

@Data
@Builder
public class ParliamentStatInputDTO {
    private long session;
    private long totalBills;
    private long passedBills;
    private long totalOpninion;
    private long[] periodChanges;
    private long[] recentChanges;
    private ParliamentStat previous;
    private ParliamentStatHistory recent;
}