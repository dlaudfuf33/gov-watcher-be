package org.govwatcher.dto.suggestion;

import lombok.Getter;

@Getter
public class SuggestionVoteRequest {
    private boolean up;
    private String visitorId;
}
