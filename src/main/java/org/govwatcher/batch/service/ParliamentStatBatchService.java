package org.govwatcher.batch.service;


import lombok.RequiredArgsConstructor;
import org.govwatcher.batch.model.ParliamentStat;
import org.govwatcher.batch.model.ParliamentStatHistory;
import org.govwatcher.batch.model.ParliamentStatLike;
import org.govwatcher.batch.repository.ParliamentStatHistoryRepository;
import org.govwatcher.batch.repository.ParliamentStatRepository;
import org.govwatcher.dto.dashboard.ParliamentStatInputDTO;
import org.govwatcher.exception.ErrorCode;
import org.govwatcher.exception.GlobalException;
import org.govwatcher.repository.BillRepository;
import org.govwatcher.repository.LegislativeNoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ParliamentStatBatchService {

    private final BillRepository billRepository;
    private final LegislativeNoticeRepository legislativeNoticeRepository;
    private final ParliamentStatRepository parliamentStatRepository;
    private final ParliamentStatHistoryRepository parliamentStatHistoryRepository;
    private final List<String> passedResults = Arrays.asList("수정가결", "원안가결");

    /**
     * 전체 회기에 대해 통계 데이터를 집계하고 저장
     * 처리 내용:
     * - 22대 이상 회기만 처리
     * - 각 회기에 대해 processSessionAggregation 호출
     */
    @Transactional
    public void aggregateAndSaveParliamentStat() {
        List<Integer> sessions = billRepository.findDistinctAges().orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        for (int session : sessions) {
            if (session < 22) {
                continue;
            }
            processSessionAggregation(session);
        }
    }

    /**
     * 단일 회기에 대한 통계 집계 및 저장
     * 처리 내용:
     * - 총 법안 수 / 통과 법안 수 / 총 의견 수 계산
     * - 직전 회기 및 7일 전 스냅샷과 비교하여 변화량/비율 계산
     * - parliament_stats 테이블에 저장
     */
    public void processSessionAggregation(int session) {

        long totalBills = billRepository.countByAge(session);
        long passedBills = billRepository.countByAgeAndResultIn(session, passedResults);
        long totalOpninion = legislativeNoticeRepository.sumOpinionCountByAge(session).orElse(0L);

        ParliamentStat previousStat = getPreviousStat(session);
        ParliamentStatHistory snapshot = getSnapshot(session);

        long[] periodChanges = calculateAllChanges(totalBills, passedBills, totalOpninion, previousStat);
        long[] recentChanges = calculateAllChanges(totalBills, passedBills, totalOpninion, snapshot);

        ParliamentStatInputDTO input = ParliamentStatInputDTO.builder()
                .session(session)
                .totalBills(totalBills)
                .passedBills(passedBills)
                .totalOpninion(totalOpninion)
                .periodChanges(periodChanges)
                .recentChanges(recentChanges)
                .previous(previousStat)
                .recent(snapshot)
                .build();

        ParliamentStat stat = buildParliamentStat(input);
        parliamentStatRepository.save(stat);
    }


    private ParliamentStat getPreviousStat(long sessionL) {
        return parliamentStatRepository.findById(sessionL - 1).orElse(null);
    }

    private ParliamentStatHistory getSnapshot(long sessionL) {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        return parliamentStatHistoryRepository
                .findBySessionAndSnapshotDate(sessionL, sevenDaysAgo)
                .orElse(null);
    }

    private ParliamentStat buildParliamentStat(ParliamentStatInputDTO input) {
        return ParliamentStat.builder()
                .session(input.getSession())
                .totalBills(input.getTotalBills())
                .passedBills(input.getPassedBills())
                .totalComments(input.getTotalOpninion())
                .billChangePeriod(input.getPeriodChanges()[0])
                .billChangeRecent(input.getRecentChanges()[0])
                .passChangePeriod(input.getPeriodChanges()[1])
                .passChangeRecent(input.getRecentChanges()[1])
                .commentsChangePeriod(input.getPeriodChanges()[2])
                .commentsChangeRecent(input.getRecentChanges()[2])
                .billChangePeriodRatio(calculateRatio(input.getPeriodChanges()[0], input.getPrevious() != null ? input.getPrevious().getTotalBills() : null))
                .billChangeRecentRatio(calculateRatio(input.getRecentChanges()[0], input.getRecent() != null ? input.getRecent().getTotalBills() : null))
                .passChangePeriodRatio(calculateRatio(input.getPeriodChanges()[1], input.getPrevious() != null ? input.getPrevious().getPassedBills() : null))
                .passChangeRecentRatio(calculateRatio(input.getRecentChanges()[1], input.getRecent() != null ? input.getRecent().getPassedBills() : null))
                .commentsChangePeriodRatio(calculateRatio(input.getPeriodChanges()[2], input.getPrevious() != null ? input.getPrevious().getTotalComments() : null))
                .commentsChangeRecentRatio(calculateRatio(input.getRecentChanges()[2], input.getRecent() != null ? input.getRecent().getTotalComments() : null))
                .build();
    }

    private double calculateRatio(long change, Long base) {
        if (base == null || base == 0L) {
            return 0.0;
        }
        return (double) change / base * 100;
    }

    private long[] calculateAllChanges(Long currentBills, Long currentPasses, Long currentComments, ParliamentStatLike previous) {
        if (previous == null) {
            return new long[]{0L, 0L, 0L};
        }
        return new long[]{
                calculateChange(currentBills, previous.getTotalBills()),
                calculateChange(currentPasses, previous.getPassedBills()),
                calculateChange(currentComments, previous.getTotalComments())
        };
    }

    private long calculateChange(Long current, Long previous) {
        if (current == null) current = 0L;
        if (previous == null) previous = 0L;
        return current - previous;
    }
}