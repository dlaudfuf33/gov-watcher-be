package org.govwatcher.dto.suggestion;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.govwatcher.dto.enums.SuggestionStatus;
import org.govwatcher.dto.enums.SuggestionType;
import org.govwatcher.model.Suggestion;

import java.time.LocalDate;

@Getter
@Builder
public class SuggestionResponse {
    private long id;
    private String name;
    private String content;
    private SuggestionType type;
    private String category;
    private SuggestionStatus status;
    private LocalDate date;
    private long votes;

    public static SuggestionResponse from(Suggestion suggestion){
        return SuggestionResponse.builder()
                .id(suggestion.getId())
                .name(suggestion.getName())
                .content(suggestion.getContent())
                .type(suggestion.getType())
                .category(suggestion.getCategory())
                .status(suggestion.getStatus())
                .date(suggestion.getCreatedAt().toLocalDate())
                .votes(suggestion.getVotes())
                .build();
    }
}
