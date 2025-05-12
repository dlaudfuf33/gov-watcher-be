package org.govwatcher.dto.politician.detail;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BillActivityDto {
    private long totalBills;
    private long recentBills;
    private double passRate;
    private List<SimpleBillDto> bills;
}
