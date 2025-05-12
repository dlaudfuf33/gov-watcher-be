package org.govwatcher.batch.service;

import lombok.RequiredArgsConstructor;
import org.govwatcher.batch.model.ParliamentStat;
import org.govwatcher.batch.model.ParliamentStatHistory;
import org.govwatcher.batch.repository.ParliamentStatHistoryRepository;
import org.govwatcher.batch.repository.ParliamentStatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ParliamentStatSnapshotService {

    private final ParliamentStatRepository parliamentStatRepository;
    private final ParliamentStatHistoryRepository parliamentStatHistoryRepository;
    /**
     * 금일 기준 전체 회기의 통계 데이터를 스냅샷 형태로 저장
     * 처리 내용:
     * - parliament_stats 테이블의 모든 회기 데이터를 조회
     * - 오늘 날짜 기준으로 parliament_stat_history에 저장
     */
    @Transactional
    public void snapshotTodayStats() {
        List<ParliamentStat> stats = parliamentStatRepository.findAll();

        LocalDate today = LocalDate.now();

        for (ParliamentStat stat : stats) {
            ParliamentStatHistory snapshot = ParliamentStatHistory.builder()
                    .session(stat.getSession())
                    .snapshotDate(today)
                    .totalBills(stat.getTotalBills())
                    .passedBills(stat.getPassedBills())
                    .totalComments(stat.getTotalComments())
                    .build();

            parliamentStatHistoryRepository.save(snapshot);
        }
    }
}
