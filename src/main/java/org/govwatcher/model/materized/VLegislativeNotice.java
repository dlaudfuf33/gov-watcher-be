package org.govwatcher.model.materized;

import com.querydsl.core.annotations.Immutable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Immutable
@Table(name = "legislative_notices_mat")
@Getter
@NoArgsConstructor
public class VLegislativeNotice {

    @Id
    @Column(name = "notice_id")
    private Long id;

    @Column(name = "bill_id")
    private String billId;

    @Column(name = "bill_title")
    private String billTitle;

    private Integer age;

    @Column(name = "propose_date")
    private LocalDate proposeDate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "opinion_count")
    private Integer opinionCount;

    @Column(name = "agree_count")
    private Integer agreeCount;

    @Column(name = "disagree_count")
    private Integer disagreeCount;

    @Column(name = "private_count")
    private Integer privateCount;

    @Column(name = "main_proposer_name")
    private String mainProposerName;

    @Column(name = "proposer_unit")
    private Integer proposerUnit;

    @Column(name = "proposer_party")
    private String proposerParty;

    @Column(name = "detail_link")
    private String detailLink;

    @Column(name = "opinion_url")
    private String opinionUrl;

}