package org.govwatcher.service;

import lombok.RequiredArgsConstructor;
import org.govwatcher.dto.enums.PrimarySortType;
import org.govwatcher.dto.enums.SecondarySortType;
import org.govwatcher.dto.legislativenotice.LegislativeNoticeDetailResponse;
import org.govwatcher.dto.legislativenotice.LegislativeNoticeResponse;
import org.govwatcher.dto.legislativenotice.ProposerDto;
import org.govwatcher.exception.ErrorCode;
import org.govwatcher.exception.GlobalException;
import org.govwatcher.model.materized.VLegislativeDetail;
import org.govwatcher.model.materized.VLegislativeNotice;
import org.govwatcher.repository.querydsl.BillPoliticianRelationQueryRepository;
import org.govwatcher.repository.querydsl.LegislativeNoticeDetailQueryRepository;
import org.govwatcher.repository.querydsl.LegislativeNoticeQueryRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LegislativeNoticeService {

    private final LegislativeNoticeQueryRepository legislativeNoticeQueryRepository;
    private final LegislativeNoticeDetailQueryRepository legislativeNoticeDetailQueryRepository;
    private final BillPoliticianRelationQueryRepository billPoliticianRelationQueryRepository;

    public Page<LegislativeNoticeResponse> getNotices(int page, int size, PrimarySortType primarySort, SecondarySortType secondarySort) {
        Page<VLegislativeNotice> entityPage = legislativeNoticeQueryRepository.findNotices(page, size, primarySort, secondarySort);
        return entityPage.map(LegislativeNoticeResponse::fromEntity);
    }

    public LegislativeNoticeDetailResponse getLegislationDetail(long id) {
        VLegislativeDetail detail = legislativeNoticeDetailQueryRepository.findbyId(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.ENTITY_NOT_FOUND));
        List<ProposerDto> proposers = billPoliticianRelationQueryRepository.findProposersByBillId(detail.getBillId(), detail.getAge());

        System.out.println(proposers.get(0).getName()+" :"+proposers.get(0).getMonaCD());

        Optional<ProposerDto> mainProposerOpt = proposers.stream()
                .filter(p -> "MAIN".equals(p.getRole()))
                .findFirst();
        ProposerDto mainProposer = mainProposerOpt.orElse(null);

        return LegislativeNoticeDetailResponse.of(detail, proposers, mainProposer);
    }
}
