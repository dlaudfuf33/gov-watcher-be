package org.govwatcher.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PartyDistributionResponse {

    private DataContainer data;

    public static PartyDistributionResponse fromEntity(List<org.govwatcher.model.PoliticianTerm> terms, int currentSession) {
        Map<String, PartyData> partyMap = new LinkedHashMap<>();

        for (org.govwatcher.model.PoliticianTerm term : terms) {
            String party = term.getParty().getName();
            if (party == null || party.isBlank()) {
                party = "기타";
            }

            PartyData partyData = partyMap.getOrDefault(party, new PartyData(party, 0, 0, 0, 0.0));
            if (term.getConstituency() != null && !term.getConstituency().equalsIgnoreCase("비례대표")) {
                partyData.localCount++;
            } else {
                partyData.proportionalCount++;
            }
            partyMap.put(party, partyData);
        }

        // 총합 계산
        int grandTotal = partyMap.values().stream()
                .mapToInt(p -> p.localCount + p.proportionalCount)
                .sum();

        // total, ratio 계산
        for (PartyData partyData : partyMap.values()) {
            partyData.total = partyData.localCount + partyData.proportionalCount;
            partyData.ratio = Math.round((partyData.total * 10000.0 / grandTotal)) / 100.0; // 소수점 2자리
        }

        DataContainer container = new DataContainer(currentSession, new ArrayList<>(partyMap.values()));
        return new PartyDistributionResponse(container);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DataContainer {
        private int currentSession;
        private List<PartyData> partyData;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartyData {
        private String party;
        private int localCount;
        private int proportionalCount;
        private int total;
        private double ratio;
    }
}