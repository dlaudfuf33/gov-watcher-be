package org.govwatcher.batch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "parliament_stats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParliamentStat implements ParliamentStatLike {
    // 국회 대수 (PK)
    @Id
    private Long session;
    private Long totalBills;
    private Long passedBills;
    private Long totalComments;

    private Long billChangePeriod;
    private Double billChangePeriodRatio;

    private Long billChangeRecent;
    private Double billChangeRecentRatio;

    private Long passChangePeriod;
    private Double passChangePeriodRatio;

    private Long passChangeRecent;
    private Double passChangeRecentRatio;

    private Long commentsChangePeriod;
    private Double commentsChangePeriodRatio;

    private Long commentsChangeRecent;
    private Double commentsChangeRecentRatio;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}