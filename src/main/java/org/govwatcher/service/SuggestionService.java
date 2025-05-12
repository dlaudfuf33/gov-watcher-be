package org.govwatcher.service;

import lombok.RequiredArgsConstructor;
import org.govwatcher.dto.enums.SuggestionStatus;
import org.govwatcher.dto.suggestion.SuggestionRequest;
import org.govwatcher.dto.suggestion.SuggestionResponse;
import org.govwatcher.exception.ErrorCode;
import org.govwatcher.exception.GlobalException;
import org.govwatcher.model.Suggestion;
import org.govwatcher.model.SuggestionVote;
import org.govwatcher.repository.SuggestionRepository;
import org.govwatcher.repository.SuggestionVoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final SuggestionVoteRepository suggestionVoteRepository;

    public void submit(SuggestionRequest request) {
        String inName = request.getName() != null ? request.getName() : "=ANONYMOUS=";
        String inEmail = request.getEmail() != null ? request.getEmail() : "=ANONYMOUS=";
        Suggestion suggestion = Suggestion.builder()
                .name(inName)
                .email(inEmail)
                .type(request.getType())
                .category(request.getCategory())
                .content(request.getContent())
                .isAnonymous(request.isAnonymous())
                .status(SuggestionStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .votes(0)
                .build();

        suggestionRepository.save(suggestion);
    }

    public List<SuggestionResponse> findByStatus(SuggestionStatus status) {
        List<Suggestion> suggestions = (status == null)
                ? suggestionRepository.findAll()
                : suggestionRepository.findByStatus(status).orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));


        return suggestions.stream()
                .map(SuggestionResponse::from)
                .toList();
    }

    @Transactional
    public void vote(Long suggestionId, String visitorId, boolean up) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        Optional<SuggestionVote> existing = suggestionVoteRepository.findBySuggestionIdAndVisitorId(suggestionId, visitorId);

        if (up) {
            if (existing.isEmpty()) {
                suggestionVoteRepository.save(SuggestionVote.builder()
                        .suggestion(suggestion)
                        .visitorId(visitorId)
                        .build());
                suggestion.setVotes(suggestion.getVotes() + 1);
            }
        } else {
            if (existing.isPresent()) {
                suggestionVoteRepository.delete(existing.get());
                suggestion.setVotes(suggestion.getVotes() - 1);
            }
        }
        suggestionRepository.save(suggestion);
    }

}
