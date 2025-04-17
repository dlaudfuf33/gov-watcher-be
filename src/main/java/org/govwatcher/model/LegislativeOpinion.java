package org.govwatcher.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "legislative_opinions", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_bill_opn", columnNames = {"bill_id", "opn_no"})
})
public class LegislativeOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "billId", nullable = false)
    private LegislativeNotice notice;

    private Long opnNo;

    @Lob
    private String subject;

    @Lob
    private String content;

    private String author;

    private Boolean isAnonymous;

    private Boolean agreement; // true: 찬성, false: 반대, null: 무응답

    @CreationTimestamp
    private LocalDateTime createdAt;
}