package org.govwatcher.batch.repository;

import org.govwatcher.batch.model.ParliamentStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParliamentStatRepository extends JpaRepository<ParliamentStat, Long> {
    Optional<Object> findBySession(long currentSession);
}
