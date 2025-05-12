package org.govwatcher.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "legislative_opinions", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_bill_opn", columnNames = {"bill_id", "opn_no"})
})
public class LegislativeOpinion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id", nullable = false)
    private LegislativeNotice notice;

    private Long opnNo;

    @Lob
    private String subject;

    @Lob
    private String content;

    private String author;

    @Enumerated(EnumType.STRING)
    private Agreement agreement;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum Agreement {
        AGREE, DISAGREE, PRIVATE
    }
}