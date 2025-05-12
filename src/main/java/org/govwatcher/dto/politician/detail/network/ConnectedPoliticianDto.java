package org.govwatcher.dto.politician.detail.network;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectedPoliticianDto {
    private Long id;
    private String name;
    private String party;
    private String district;
    private Long bills;
}
