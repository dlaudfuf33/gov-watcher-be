package org.govwatcher.repository;

import org.govwatcher.dto.dashboard.PoliticianSimpleDTO;
import org.govwatcher.model.PoliticianTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PoliticianTermsRepository extends JpaRepository<PoliticianTerm, Long> {
    Optional<PoliticianTerm> findTop1ByOrderByUnitDesc();

    @Query("""
                SELECT new org.govwatcher.dto.dashboard.PoliticianSimpleDTO(
                    p.id,
                    p.birthDate,
                    p.gender
                )
                FROM PoliticianTerm pt
                JOIN pt.politician p
                WHERE pt.unit = (
                    SELECT MAX(sub.unit)
                    FROM PoliticianTerm sub
                )
                AND pt.jobTitle IS NOT NULL
                AND pt.jobTitle <> ''
            """)
    List<PoliticianSimpleDTO> findForDemographicStats();

    @Query("""
                SELECT pt
                FROM PoliticianTerm pt
                WHERE pt.unit = (
                    SELECT MAX(sub.unit)
                    FROM PoliticianTerm sub
                ) and pt.jobTitle != '' 
            """)
    Optional<List<PoliticianTerm>> findForPartyDistribution();
}
