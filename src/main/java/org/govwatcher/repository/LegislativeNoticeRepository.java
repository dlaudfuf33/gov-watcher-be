package org.govwatcher.repository;

import org.govwatcher.model.LegislativeNotice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LegislativeNoticeRepository extends JpaRepository<LegislativeNotice, Long> {
    @Query("SELECT SUM(l.opinionCount) " +
            "FROM Bill b " +
            "JOIN LegislativeNotice l ON b.id = l.bill.id " +
            "WHERE b.age = :age")
    Optional<Long> sumOpinionCountByAge(@Param("age") int age);

}
