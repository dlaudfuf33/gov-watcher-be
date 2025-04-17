package org.govwatcher.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "politicians", indexes = {
        @Index(name = "idx_politician_lookup", columnList = "name, hanja_name")
})
public class Politician {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mona_cd", nullable = false, unique = true)
    private String monaCd;

    private String name;
    private String hanjaName;
    private String engName;

    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "politician", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PoliticianTerm> terms = new ArrayList<>();

    @OneToOne(mappedBy = "politician", cascade = CascadeType.ALL, orphanRemoval = true)
    private PoliticianContact contact;

    @OneToOne(mappedBy = "politician", cascade = CascadeType.ALL, orphanRemoval = true)
    private PoliticianSns sns;

    @OneToOne(mappedBy = "politician", cascade = CascadeType.ALL, orphanRemoval = true)
    private PoliticianCareer career;

    public enum Gender {
        male, female
    }
}