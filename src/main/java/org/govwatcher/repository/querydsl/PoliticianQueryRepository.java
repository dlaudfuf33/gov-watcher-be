package org.govwatcher.repository.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.govwatcher.dto.politician.PoliticianListResponse;
import org.govwatcher.dto.politician.PoliticiansResponse;
import org.govwatcher.dto.politician.detail.PoliticianTermDto;
import org.govwatcher.dto.politician.detail.SimpleBillDto;
import org.govwatcher.dto.politician.detail.network.CenterPoliticianDto;
import org.govwatcher.dto.politician.detail.network.ConnectedPoliticianDto;
import org.govwatcher.dto.politician.detail.network.NetworkEdgeDto;
import org.govwatcher.dto.politician.detail.network.PoliticianNetworkDto;
import org.govwatcher.model.*;
import org.govwatcher.model.materized.QVPoliticianDetail;
import org.govwatcher.model.materized.QVPoliticians;
import org.govwatcher.model.materized.VPoliticianDetail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PoliticianQueryRepository {
    private final JPAQueryFactory queryFactory;

    public PoliticianListResponse findByConditions(int page, int size, String name, String party, String district, String sort) {
        QVPoliticians p = QVPoliticians.vPoliticians;

        BooleanBuilder builder = new BooleanBuilder();
        if (!name.isBlank()) builder.and(p.name.containsIgnoreCase(name));
        if (!party.isBlank()) builder.and(p.partyName.eq(party));
        if (!district.isBlank()) builder.and(p.district.containsIgnoreCase(district));
        builder.and(p.term.eq(22));

        // 정렬 조건
        OrderSpecifier<?> orderSpecifier = switch (sort) {
            case "passRate" -> p.passedBills.multiply(1.0)
                    .divide(p.totalBills.nullif(0L))
                    .desc();
            default -> p.recentBills.desc();
        };

        List<PoliticiansResponse> results = queryFactory
                .selectFrom(p)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset((long) (page - 1) * size)
                .limit(size)
                .fetch()
                .stream()
                .map(PoliticiansResponse::fromEntity)
                .toList();

        Long total = queryFactory
                .select(p.count())
                .from(p)
                .where(builder)
                .fetchOne();

        return PoliticianListResponse.builder()
                .data(results)
                .total(total)
                .build();
    }

    public Optional<VPoliticianDetail> findbyPoliticianId(Long id) {
        QVPoliticianDetail p = QVPoliticianDetail.vPoliticianDetail;
        VPoliticianDetail result = queryFactory
                .selectFrom(p)
                .where(p.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    public Optional<String> findCareerByPoliticianId(Long id) {
        QPoliticianCareer c = QPoliticianCareer.politicianCareer;

        return Optional.ofNullable(
                queryFactory
                        .select(Expressions.stringTemplate("CAST({0} AS text)", c.career))
                        .from(c)
                        .where(c.politicianId.eq(id))
                        .fetchOne()
        );
    }

    public List<PoliticianTermDto> findTermsByPoliticianId(Long id) {
        QPoliticianTerm t = QPoliticianTerm.politicianTerm;
        QCommittee c = QCommittee.committee;
        List<PoliticianTermDto> result = queryFactory
                .select(Projections.constructor(
                        PoliticianTermDto.class,
                        t.unit,
                        t.party.name,
                        t.constituency,
                        t.jobTitle,
                        c.name
                ))
                .from(t)
                .leftJoin(c).on(t.committee.id.eq(c.id))
                .where(t.politician.id.eq(id))
                .fetch();

        return result != null ? result : List.of();
    }


    public List<SimpleBillDto> findBillsByPoliticianId(Long id) {
        QPolitician p = QPolitician.politician;
        QBillPoliticianRelation bpr = QBillPoliticianRelation.billPoliticianRelation;
        QBill b = QBill.bill;

        List<SimpleBillDto> result = queryFactory.select(Projections.constructor(
                        SimpleBillDto.class,
                        b.title,
                        b.currentStep
                ))
                .from(b)
                .join(bpr).on(bpr.bill.id.eq(b.id))
                .join(p).on(bpr.politician.id.eq(p.id))
                .where(p.id.eq(id))
                .orderBy(b.proposeDate.desc())
                .limit(3)
                .fetch();

        return result != null ? result : List.of();
    }

    public PoliticianNetworkDto findPoliticianNetwork(Long id) {
        QBillPoliticianRelation bpr1 = new QBillPoliticianRelation("bpr1");
        QBillPoliticianRelation bpr2 = new QBillPoliticianRelation("bpr2");
        QPolitician p = QPolitician.politician;
        QPoliticianTerm t = QPoliticianTerm.politicianTerm;
        QParty pt = QParty.party;

        // 1. 이 정치인이 참여한 bill 목록
        List<Long> billIds = queryFactory
                .select(bpr1.bill.id)
                .from(bpr1)
                .where(bpr1.politician.id.eq(id))
                .fetch();

        // 2. 공동발의한 정치인 리스트 및 공동발의 횟수
        List<ConnectedPoliticianDto> connected = queryFactory
                .select(Projections.constructor(
                        ConnectedPoliticianDto.class,
                        bpr2.politician.id,
                        bpr2.politician.name,
                        pt.name,              // 정당명
                        t.constituency,       // 지역구
                        bpr2.politician.id.count() // 공동발의 횟수
                ))
                .from(bpr2)
                .join(t).on(t.politician.id.eq(bpr2.politician.id).and(t.unit.eq(22)))
                .join(pt).on(t.party.id.eq(pt.id))
                .where(bpr2.bill.id.in(billIds)
                        .and(bpr2.politician.id.ne(id)))
                .groupBy(bpr2.politician.id, bpr2.politician.name, pt.name, t.constituency)
                .fetch();

        // 3. edge 정보: 공동발의한 횟수
        List<NetworkEdgeDto> edges = connected.stream()
                .map(dto -> NetworkEdgeDto.builder()
                        .from(id)
                        .to(dto.getId())
                        .value(dto.getBills())
                        .build())
                .toList();

        // 4. centerPolitician 정보
        Tuple center = queryFactory
                .select(
                        p.id,
                        p.name,
                        pt.name,
                        t.constituency,
                        bpr1.id.countDistinct()
                )
                .from(p)
                .join(t).on(t.politician.id.eq(p.id).and(t.unit.eq(22)))
                .join(pt).on(t.party.id.eq(pt.id))
                .join(bpr1).on(bpr1.politician.id.eq(p.id))
                .where(p.id.eq(id))
                .groupBy(p.id, p.name, pt.name, t.constituency)
                .fetchOne();

        CenterPoliticianDto centerDto = CenterPoliticianDto.builder()
                .id(center.get(p.id))
                .name(center.get(p.name))
                .party(center.get(pt.name))
                .district(center.get(t.constituency))
                .bills(center.get(bpr1.id.countDistinct()))
                .build();

        return PoliticianNetworkDto.builder()
                .centerPolitician(centerDto)
                .connectedPoliticians(connected)
                .edges(edges)
                .build();
    }
}