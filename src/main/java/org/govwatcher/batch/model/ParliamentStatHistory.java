package org.govwatcher.batch.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "parliament_stat_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParliamentStatHistory implements ParliamentStatLike {

    // 스냅샷 고유 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 국회 대수
    private Long session;

    // 스냅샷 저장 기준일
    private LocalDate snapshotDate;

    // 스냅샷 시점 총 발의 법안 수
    private Long totalBills;

    // 스냅샷 시점 총 통과 법안 수
    private Long passedBills;

    // 스냅샷 시점 총 시민 의견 수
    private Long totalComments;

    // 생성 일시 (자동 기록)
    @CreationTimestamp
    private LocalDateTime createdAt;
}
