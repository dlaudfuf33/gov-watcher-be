package org.govwatcher.batch.repository;

import org.govwatcher.batch.model.ParliamentStatHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ParliamentStatHistoryRepository extends JpaRepository<ParliamentStatHistory,Long> {
    Optional<ParliamentStatHistory> findBySessionAndSnapshotDate(Long session, LocalDate snapshotDate);

}
