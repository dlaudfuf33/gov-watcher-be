package org.govwatcher.dto.politician;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class PoliticianListResponse {
    private List<PoliticiansResponse> data;
    private Long total;
}