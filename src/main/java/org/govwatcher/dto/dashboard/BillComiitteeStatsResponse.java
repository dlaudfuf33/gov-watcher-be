package org.govwatcher.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class BillComiitteeStatsResponse {
    private DataContainer data;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContainer {
        private Long total;
        private List<CategoryCount> categories;
    }

    public static BillComiitteeStatsResponse fromEntity(List<CategoryCount> categoryCounts) {
        long total = categoryCounts.stream()
                .mapToLong(CategoryCount::getValue)
                .sum();

        return new BillComiitteeStatsResponse(new DataContainer(total, categoryCounts));
    }
}