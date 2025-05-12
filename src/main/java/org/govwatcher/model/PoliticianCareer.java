package org.govwatcher.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
@Getter
@Entity
@Table(name = "politician_careers")
public class PoliticianCareer {

    @Id
    private Long politicianId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "politician_id")
    private Politician politician;

    @Column(name = "career", columnDefinition = "LONGTEXT")
    @Lob
    private String career;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}