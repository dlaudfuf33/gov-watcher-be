package org.govwatcher.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "bills", indexes = {
        @Index(name = "idx_bill_no", columnList = "bill_no")
})
@Access(AccessType.FIELD)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_id", nullable = false, unique = true)
    private String billId;

    @Column
    private String billNo;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "committee_id")
    private Committee committee;

    @Column
    private int age;

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillStatusFlow> statusFlows = new ArrayList<>();

    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillPoliticianRelation> proposerRelations = new ArrayList<>();

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private LegislativeNotice legislativeNotice;

    @Lob
    private String proposer;

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