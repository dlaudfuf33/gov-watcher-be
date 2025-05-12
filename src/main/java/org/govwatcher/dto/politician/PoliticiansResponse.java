package org.govwatcher.dto.politician;

import lombok.Builder;
import lombok.Getter;
import org.govwatcher.model.materized.VPoliticians;

@Getter
@Builder
public class PoliticiansResponse {
    private Long id;
    private String monaCD;
    private String name;
    private String party;
    private String district;
    private String position;
    private int term;
    private String profileImage;
    private Long totalBills;
    private Long recentBills;
    private Long passedBills;
    private double passRate;

    public static PoliticiansResponse fromEntity(VPoliticians p) {
        long passed = p.getPassedBills() != null ? p.getPassedBills() : 0;
        long total = p.getTotalBills() != null ? p.getTotalBills() : 0;
        double rate = total > 0 ? Math.round(passed * 1000.0 / total) / 10.0 : 0.0;

        return PoliticiansResponse.builder()
                .id(p.getId())
                .monaCD(p.getMonaCD())
                .name(p.getName())
                .party(p.getPartyName())
                .district(p.getDistrict())
                .position((p.getCommitteeName() != null ? p.getCommitteeName() + " " : "") + p.getPosition())
                .term(p.getTerm())
                .profileImage(p.getProfileImage())
                .totalBills(p.getTotalBills())
                .recentBills(p.getRecentBills())
                .passedBills(p.getPassedBills())
                .passRate(rate)
                .build();
    }
}
