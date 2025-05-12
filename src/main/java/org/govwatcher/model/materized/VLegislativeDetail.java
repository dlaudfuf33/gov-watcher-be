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
@Table(name = "legislative_notice_detail_mat")
@Getter
@NoArgsConstructor
public class VLegislativeDetail {

    @Id
    @Column(name = "notice_id")
    private Long id;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "age")
    private int age;

    @Column(name = "bill_no")
    private String billNo;

    @Column(name = "bill_title")
    private String billTitle;

    @Column(name = "summary")
    private String summary;

    @Column(name = "propose_date")
    private LocalDate proposeDate;

    @Column(name = "detail_link")
    private String detailLink;

    @Column(name = "opinion_url")
    private String opinionUrl;

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

    @Column(name = "committee_name")
    private String committeeName;

}