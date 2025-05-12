package org.govwatcher.dto.politician.detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoliticianTermDto {
    private int unit;
    private String party;
    private String constituency;
    private String jobTitle;
    private String committees;
}