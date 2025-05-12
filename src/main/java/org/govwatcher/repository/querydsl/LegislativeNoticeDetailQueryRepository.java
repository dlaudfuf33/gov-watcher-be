package org.govwatcher.repository.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.govwatcher.model.materized.QVLegislativeDetail;
import org.govwatcher.model.materized.VLegislativeDetail;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LegislativeNoticeDetailQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<VLegislativeDetail> findbyId(long id) {
        QVLegislativeDetail detail = QVLegislativeDetail.vLegislativeDetail;

        VLegislativeDetail result = queryFactory
                .selectFrom(detail)
                .where(detail.id.eq(id))
                .limit(1)
                .fetchOne();

        return Optional.ofNullable(result);
    }

}
