package org.govwatcher.repository;

import org.govwatcher.dto.dashboard.CategoryCount;
import org.govwatcher.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {


    @Query("""
                SELECT new org.govwatcher.dto.dashboard.CategoryCount(
                    b.committee.name,
                    COUNT(b)
                )
                FROM Bill b
                WHERE b.age = (
                    SELECT MAX(b2.age) FROM Bill b2
                )
                GROUP BY b.committee.name
            """)
    List<CategoryCount> countBillsGroupedByCommittee();

    @Query("""
                SELECT new org.govwatcher.dto.dashboard.CategoryCount(
                    COALESCE(pt.party.name, '무소속'),
                    COUNT(b)
                )
                FROM Bill b
                JOIN BillPoliticianRelation bpr
                    ON bpr.bill = b AND bpr.role = org.govwatcher.model.BillPoliticianRelation.Role.MAIN
                JOIN PoliticianTerm pt
                    ON pt.politician = bpr.politician AND pt.unit = b.age
                WHERE b.age = (
                    SELECT MAX(b2.age) FROM Bill b2
                )
                GROUP BY pt.party.name
            """)
    List<CategoryCount> countBillsGroupedByParty();

    @Query("SELECT DISTINCT b.age FROM Bill b WHERE b.age!=0 or b.age is null")
    Optional<List<Integer>> findDistinctAges();

    long countByAge(int age);

    long countByAgeAndResultIn(int age, List<String> results);
}
