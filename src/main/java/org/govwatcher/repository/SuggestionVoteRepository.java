package org.govwatcher.repository;

import org.govwatcher.model.SuggestionVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SuggestionVoteRepository extends JpaRepository<SuggestionVote, Long> {
    Optional<SuggestionVote> findBySuggestionIdAndVisitorId(Long suggestionId, String visitorId);
}
