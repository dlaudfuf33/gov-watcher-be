package org.govwatcher.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SuggestionStatus {
    PENDING, REVIEWING, COMPLETED, REJECTED;

    @JsonCreator
    public static SuggestionStatus from(String value) {
        return SuggestionStatus.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
