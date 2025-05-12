package org.govwatcher.dto.legislativenotice;

import lombok.Getter;
import org.govwatcher.model.materized.VLegislativeNotice;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class LegislativeNoticeResponse {
    private Long id;
    private String title;
    private String proposer;
    private String party;
    private String date;
    private int opinionCounts;
    private int agree;
    private int disagree;
    private int agreeRatio;
    private int disagreeRatio;
    private int daysLeft;
    private String opinionUrl;
    private String detailUrl;

    public static LegislativeNoticeResponse fromEntity(VLegislativeNotice entity) {
        int agreeCount = entity.getAgreeCount() != null ? entity.getAgreeCount() : 0;
        int disagreeCount = entity.getDisagreeCount() != null ? entity.getDisagreeCount() : 0;
        int privateCount = entity.getDisagreeCount() != null ? entity.getPrivateCount() : 0;
        int total = agreeCount + disagreeCount + privateCount;

        LegislativeNoticeResponse dto = new LegislativeNoticeResponse();
        dto.id = entity.getId();
        dto.title = entity.getBillTitle() != null ? entity.getBillTitle() : "(제목 없음)";
        dto.proposer = entity.getMainProposerName() != null ? entity.getMainProposerName() : "(알 수 없음)";
        dto.party = entity.getProposerParty() != null ? entity.getProposerParty() : "(무소속)";
        dto.date = entity.getProposeDate() != null ? entity.getProposeDate().toString() : "";

        dto.opinionCounts = total;
        dto.agree = agreeCount;
        dto.disagree = disagreeCount;
        dto.agreeRatio = total > 0 ? (agreeCount * 100) / total : 0;
        dto.disagreeRatio = total > 0 ? (disagreeCount * 100) / total : 0;

        if (entity.getEndDate() != null) {
            int daysLeft = (int) ChronoUnit.DAYS.between(LocalDate.now(), entity.getEndDate());
            dto.daysLeft = Math.max(daysLeft, 0);
        } else {
            dto.daysLeft = 0;
        }

        dto.opinionUrl = entity.getOpinionUrl() != null ? entity.getOpinionUrl() : "";
        dto.detailUrl = entity.getDetailLink() != null ? entity.getDetailLink() : "";

        return dto;
    }
}
