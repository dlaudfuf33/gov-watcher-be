package org.govwatcher.model.materized;

import com.querydsl.core.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Immutable
@Table(name = "politicians_mat")
@Getter
@NoArgsConstructor
public class VPoliticians {

    @Id
    @Column(name = "politician_id")
    private Long id;

    @Column(name = "mona_cd")
    private String monaCD;

    @Column(name = "name")
    private String name;

    @Column(name = "party_name")
    private String partyName;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "district")
    private String district;

    @Column(name = "position")
    private String position;

    @Column(name = "term")
    private int term;

    @Column(name = "committee_name")
    private String committeeName;

    @Column(name = "total_bills")
    private Long totalBills;

    @Column(name = "recent_bills")
    private Long recentBills;

    @Column(name = "passed_bills")
    private Long passedBills;
}