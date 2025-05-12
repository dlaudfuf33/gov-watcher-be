package org.govwatcher.dto.politician.detail.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PoliticianNetworkDto {
    private CenterPoliticianDto centerPolitician;
    private List<ConnectedPoliticianDto> connectedPoliticians;
    private List<NetworkEdgeDto> edges;
}