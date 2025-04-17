package org.govwatcher.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "politician_terms", uniqueConstraints = {
        @UniqueConstraint(name = "uniq_term", columnNames = {"politician_id", "unit"})
})
public class PoliticianTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "politician_id", nullable = false)
    private Politician politician;

    private int unit;
    private String party;
    private String constituency;
    private String reelected;
    private String jobTitle;
    private String committeeMain;

    @Lob
    private String committees;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}