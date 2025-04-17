package org.govwatcher.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "politician_contacts")
public class PoliticianContact {

    @Id
    private Long politicianId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "politician_id")
    private Politician politician;

    private String phone;
    private String email;
    private String homepage;
    private String officeRoom;
    private String staff;
    private String secretary;
    private String secretary2;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}