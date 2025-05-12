package org.govwatcher.dto.politician.detail.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NetworkEdgeDto {
    private Long from;
    private Long to;
    private Long value;
}