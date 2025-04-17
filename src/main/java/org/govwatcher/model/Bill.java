package org.govwatcher.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bills", indexes = {
        @Index(name = "idx_bill_no", columnList = "bill_no")
})
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_id", nullable = false, unique = true)
    private String billId;

    private String billNo;
    private String name;
    private String committee;
    private String committeeId;
    private String age;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillStatusFlow> statusFlows = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillPoliticianRelation> proposerRelations = new ArrayList<>();

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private LegislativeNotice legislativeNotice;

    @Lob
    private String proposer;

    private String mainProposer;

    @Lob
    private String subProposers;

    private LocalDate proposeDate;

    private LocalDate lawProcDate;
    private LocalDate lawPresentDate;
    private LocalDate lawSubmitDate;
    private LocalDate cmtProcDate;
    private LocalDate cmtPresentDate;
    private LocalDate committeeDate;
    private LocalDate procDate;

    private String result;
    private String lawProcResultCd;
    private String cmtProcResultCd;

    @Lob
    private String detailLink;

    @Lob
    private String stepLog;

    @Lob
    private String summary;

    private String currentStep;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

}