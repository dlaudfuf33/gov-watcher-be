package org.govwatcher.dto.suggestion;

import lombok.Data;
import org.govwatcher.dto.enums.SuggestionType;
import org.govwatcher.model.Suggestion;

@Data
public class SuggestionRequest {
    private String name;
    private String email;
    private SuggestionType type;
    private String category;
    private String content;
    private boolean isAnonymous;

}
