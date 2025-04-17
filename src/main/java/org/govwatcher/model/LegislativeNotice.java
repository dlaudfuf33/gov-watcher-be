package org.govwatcher.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "legislative_notices")
public class LegislativeNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "billId", nullable = false)
    private Bill bill;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LegislativeOpinion> opinions = new ArrayList<>();

    private LocalDate startDate;
    private LocalDate endDate;

    @Lob
    private String commentsUrl;

    private Integer commentsCount;
    private Integer viewCount;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}