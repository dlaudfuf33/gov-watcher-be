package org.govwatcher.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParliamentStatResponse {
    private DataContainer data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContainer {
        private int currentSession;
        private List<ParliamentStat> parliamentStat;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ParliamentStat {
        private String title;
        private int value;
        private int changePeriod;
        private int changeRecent;
        private double changePeriodRatio;
        private double changeRecentRatio;
    }

    public static ParliamentStatResponse fromEntity(org.govwatcher.batch.model.ParliamentStat entity) {
        DataContainer data = new DataContainer(
                entity.getSession().intValue(),
                List.of(
                        new ParliamentStat("발의 법안 수",
                                entity.getTotalBills().intValue(),
                                entity.getBillChangePeriod().intValue(),
                                entity.getBillChangeRecent().intValue(),
                                round(entity.getBillChangePeriodRatio()),
                                round(entity.getBillChangeRecentRatio())
                        ),
                        new ParliamentStat("통과 법안 수",
                                entity.getPassedBills().intValue(),
                                entity.getPassChangePeriod().intValue(),
                                entity.getPassChangeRecent().intValue(),
                                round(entity.getPassChangePeriodRatio()),
                                round(entity.getPassChangeRecentRatio())
                        ),
                        new ParliamentStat("시민 의견 수",
                                entity.getTotalComments().intValue(),
                                entity.getCommentsChangePeriod().intValue(),
                                entity.getCommentsChangeRecent().intValue(),
                                round(entity.getCommentsChangePeriodRatio()),
                                round(entity.getCommentsChangeRecentRatio())
                        ))
        );
        return new ParliamentStatResponse(data);
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}