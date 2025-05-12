package org.govwatcher.repository.querydsl;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.govwatcher.dto.enums.PrimarySortType;
import org.govwatcher.dto.enums.SecondarySortType;
import org.govwatcher.model.materized.QVLegislativeNotice;
import org.govwatcher.model.materized.VLegislativeNotice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class LegislativeNoticeQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Page<VLegislativeNotice> findNotices(int page, int size,
                                                PrimarySortType primarySort,
                                                SecondarySortType secondarySort) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);

        QVLegislativeNotice notice = QVLegislativeNotice.vLegislativeNotice;

        var whereClause = notice.endDate.gt(LocalDate.now());

        OrderSpecifier<?> primaryOrder = switch (primarySort) {
            case DEADLINE -> notice.endDate.asc();
            case POPULAR -> notice.opinionCount.desc();
        };

        OrderSpecifier<?> secondaryOrder = notice.id.desc();
        switch (secondarySort) {
            case OPINIONS -> secondaryOrder = notice.opinionCount.desc();
            case AGREE -> {
                NumberExpression<Double> total = notice.agreeCount.add(notice.disagreeCount).doubleValue();
                NumberExpression<Double> agreeRate = new CaseBuilder()
                        .when(total.gt(0))
                        .then(notice.agreeCount.doubleValue().divide(total))
                        .otherwise(0.0);
                secondaryOrder = agreeRate.desc();
            }
            case DISAGREE -> {
                NumberExpression<Double> total = notice.agreeCount.add(notice.disagreeCount).doubleValue();
                NumberExpression<Double> disagreeRate = new CaseBuilder()
                        .when(total.gt(0))
                        .then(notice.disagreeCount.doubleValue().divide(total))
                        .otherwise(0.0);
                secondaryOrder = disagreeRate.desc();
            }
            case NONE -> secondaryOrder = notice.id.desc();
        }

        JPAQuery<VLegislativeNotice> contentQuery = queryFactory
                .selectFrom(notice)
                .where(whereClause)
                .orderBy(primaryOrder, secondaryOrder)
                .offset((long) (safePage - 1) * safeSize)
                .limit(safeSize);

        List<VLegislativeNotice> results = contentQuery.fetch();

        long total = Optional.ofNullable(
                queryFactory.select(notice.count())
                        .from(notice)
                        .where(whereClause)
                        .fetchOne()
        ).orElse(0L);

        return PageableExecutionUtils.getPage(
                results,
                PageRequest.of(safePage - 1, safeSize),
                () -> total
        );
    }
}
