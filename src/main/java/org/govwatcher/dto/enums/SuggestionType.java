package org.govwatcher.dto.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SuggestionType {
    FEATURE, BUG, IMPROVEMENT, OTHER;

    @JsonCreator
    public static SuggestionType from(String value) {
        return SuggestionType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
