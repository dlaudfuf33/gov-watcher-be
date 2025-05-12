package org.govwatcher.dto.politician.detail;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SnsDto {
    private String twitter;
    private String facebook;
    private String youtube;
}
