package org.govwatcher.service;

import lombok.RequiredArgsConstructor;
import org.govwatcher.batch.model.ParliamentStat;
import org.govwatcher.batch.repository.ParliamentStatRepository;
import org.govwatcher.dto.dashboard.*;
import org.govwatcher.exception.ErrorCode;
import org.govwatcher.exception.GlobalException;
import org.govwatcher.model.PoliticianTerm;
import org.govwatcher.repository.BillRepository;
import org.govwatcher.repository.PoliticianTermsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final PoliticianTermsRepository politicianTermsRepository;
    private final ParliamentStatRepository parliamentStatRepository;
    private final BillRepository billRepository;

    @Transactional(readOnly = true)
    public ParliamentStatResponse getParliamentStats() {
        PoliticianTerm foundPoliticianTerm = politicianTermsRepository.findTop1ByOrderByUnitDesc().orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));
        long currentSession = foundPoliticianTerm.getUnit();

        ParliamentStat parliamentStat = parliamentStatRepository.findById(currentSession)
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));
        return ParliamentStatResponse.fromEntity(parliamentStat);
    }

    @Transactional(readOnly = true)
    public PartyDistributionResponse getPartyDistribution() {
        List<PoliticianTerm> terms = politicianTermsRepository.findForPartyDistribution()
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));
        int currentSession = terms.get(0).getUnit();

        return PartyDistributionResponse.fromEntity(terms, currentSession);
    }

    public DemographicStatsResponse getDemographicStats() {
        List<PoliticianSimpleDTO> terms = politicianTermsRepository.findForDemographicStats();
        return DemographicStatsResponse.fromEntity(terms);
    }

    public BillComiitteeStatsResponse getCommitteeStats() {
        List<CategoryCount> committees = billRepository.countBillsGroupedByCommittee();
        long total = committees.stream()
                .mapToLong(CategoryCount::getValue)
                .sum();

        BillComiitteeStatsResponse.DataContainer dataContainer = new BillComiitteeStatsResponse.DataContainer(total, committees);
        return new BillComiitteeStatsResponse(dataContainer);
    }

    public BillComiitteeStatsResponse gePartyBillStats() {
        List<CategoryCount> committees = billRepository.countBillsGroupedByParty();
        long total = committees.stream()
                .mapToLong(CategoryCount::getValue)
                .sum();

        BillComiitteeStatsResponse.DataContainer dataContainer = new BillComiitteeStatsResponse.DataContainer(total, committees);
        return new BillComiitteeStatsResponse(dataContainer);
    }
}

