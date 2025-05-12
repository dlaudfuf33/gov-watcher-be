package org.govwatcher.dto.legislativenotice;

import lombok.Builder;
import lombok.Getter;
import org.govwatcher.model.materized.VLegislativeDetail;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Getter
@Builder
public class LegislativeNoticeDetailResponse {
    private long id;
    private String billNo;
    private String title;
    private String summary;
    private int opinionCounts;
    private int daysLeft;
    private String mainProposer;
    private String mainProposerParty;
    private String mainProposerImageUrl;
    private List<ProposerDto> proposers;
    private LocalDate proposerDate;
    private String committee;
    private int agree;
    private int disagree;
    private int agreeRatio;
    private int disagreeRatio;
    private String detailUrl;
    private String opinionUrl;

    public static LegislativeNoticeDetailResponse of(VLegislativeDetail vLegislativeDetail, List<ProposerDto> proposers,ProposerDto mainProposer) {
        int agreeCount = vLegislativeDetail.getAgreeCount() != null ? vLegislativeDetail.getAgreeCount() : 0;
        int disagreeCount = vLegislativeDetail.getDisagreeCount() != null ? vLegislativeDetail.getDisagreeCount() : 0;
        int privateCount = vLegislativeDetail.getPrivateCount() != null ? vLegislativeDetail.getPrivateCount() : 0;
        int total = agreeCount + disagreeCount + privateCount;
        int daysLeft;
        if (vLegislativeDetail.getEndDate() != null) {
            daysLeft = (int) ChronoUnit.DAYS.between(LocalDate.now(), vLegislativeDetail.getEndDate());
            daysLeft = Math.max(daysLeft, 0);
        } else {
            daysLeft = 0;
        }
        return LegislativeNoticeDetailResponse.builder()
                .id(vLegislativeDetail.getId())
                .billNo(vLegislativeDetail.getBillNo())
                .title(vLegislativeDetail.getBillTitle())
                .summary(vLegislativeDetail.getSummary())
                .opinionCounts(total)
                .daysLeft(daysLeft)
                .mainProposer(mainProposer.getName())
                .mainProposerParty(mainProposer.getParty())
                .mainProposerImageUrl(mainProposer.getImageUrl())
                .proposers(proposers)
                .proposerDate(vLegislativeDetail.getProposeDate())
                .committee(vLegislativeDetail.getCommitteeName())
                .agree(agreeCount)
                .disagree(disagreeCount)
                .agreeRatio(total > 0 ? (agreeCount * 100) / total : 0)
                .disagreeRatio(total > 0 ? (disagreeCount * 100) / total : 0)
                .detailUrl(vLegislativeDetail.getDetailLink())
                .opinionUrl(vLegislativeDetail.getOpinionUrl())
                .build();
    }
}