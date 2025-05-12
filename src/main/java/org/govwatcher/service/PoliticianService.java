package org.govwatcher.service;

import lombok.RequiredArgsConstructor;
import org.govwatcher.dto.politician.PoliticianListResponse;
import org.govwatcher.dto.politician.detail.PoliticianDetailResponse;
import org.govwatcher.dto.politician.detail.PoliticianTermDto;
import org.govwatcher.dto.politician.detail.SimpleBillDto;
import org.govwatcher.dto.politician.detail.network.PoliticianNetworkDto;
import org.govwatcher.exception.ErrorCode;
import org.govwatcher.exception.GlobalException;
import org.govwatcher.model.materized.VPoliticianDetail;
import org.govwatcher.repository.querydsl.PoliticianQueryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PoliticianService {

    private final PoliticianQueryRepository politicianQueryRepository;

    public PoliticianListResponse getPoliticians(int page, int size, String name, String party, String district, String sort) {
        return politicianQueryRepository.findByConditions(page, size, name, party, district, sort);
    }

    public PoliticianDetailResponse getPoliticiansDetail(Long id) {
        VPoliticianDetail mat = politicianQueryRepository.findbyPoliticianId(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));

        String career = politicianQueryRepository.findCareerByPoliticianId(id).orElse("").trim();
        List<PoliticianTermDto> terms = safeList(politicianQueryRepository.findTermsByPoliticianId(id));
        List<SimpleBillDto> bills = safeList(politicianQueryRepository.findBillsByPoliticianId(id));

        return PoliticianDetailResponse.of(mat, career, terms, bills);
    }

    public PoliticianNetworkDto getPoliticianNetwork(Long politicianId) {
        return politicianQueryRepository.findPoliticianNetwork(politicianId);
    }

    private <T> List<T> safeList(List<T> list) {
        return list != null ? list : List.of();
    }
}
