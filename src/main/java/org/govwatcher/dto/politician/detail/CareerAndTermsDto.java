package org.govwatcher.dto.politician.detail;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class CareerAndTermsDto {
    private String career;
    private List<PoliticianTermDto> terms;
}
