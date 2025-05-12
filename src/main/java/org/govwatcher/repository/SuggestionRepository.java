package org.govwatcher.repository;

import org.govwatcher.dto.enums.SuggestionStatus;
import org.govwatcher.model.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
    Optional<List<Suggestion>> findByStatus(SuggestionStatus status);
}
