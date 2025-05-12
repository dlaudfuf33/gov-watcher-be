package org.govwatcher.repository.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.govwatcher.dto.legislativenotice.ProposerDto;
import org.govwatcher.model.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BillPoliticianRelationQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<ProposerDto> findProposersByBillId(Long billId, Integer age) {
        QBillPoliticianRelation bpr = QBillPoliticianRelation.billPoliticianRelation;
        QPolitician p = QPolitician.politician;
        QPoliticianTerm pt = QPoliticianTerm.politicianTerm;
        QParty party = QParty.party;

        return queryFactory
                .select(
                        p.id,
                        p.name,
                        p.monaCd,
                        p.profileImage,
                        party.name,
                        bpr.role
                )
                .from(bpr)
                .join(bpr.politician, p)
                .join(pt).on(pt.politician.eq(p).and(pt.unit.eq(age)))
                .leftJoin(pt.party, party)
                .where(bpr.bill.id.eq(billId))
                .fetch()
                .stream()
                .map(tuple -> {
                    Long idValue = tuple.get(p.id);
                    long id = idValue != null ? idValue : 0L;

                    String name = tuple.get(p.name) != null ? tuple.get(p.name) : "(알 수 없음)";
                    String imageUrl = tuple.get(p.profileImage) != null ? tuple.get(p.profileImage) : "";
                    String partyName = tuple.get(party.name) != null ? tuple.get(party.name) : "(무소속)";
                    String monaCd = tuple.get(p.monaCd) != null ? tuple.get(p.monaCd) : "";

                    BillPoliticianRelation.Role roleEnum = tuple.get(bpr.role);
                    String role = roleEnum != null ? roleEnum.name() : "UNKNOWN";

                    return ProposerDto.builder()
                            .id(id)
                            .name(name)
                            .monaCD(monaCd)
                            .imageUrl(imageUrl)
                            .party(partyName)
                            .role(role)
                            .build();
                })
                .toList();
    }

}