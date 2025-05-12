package org.govwatcher.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "suggestion_votes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"suggestion_id", "visitor_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuggestionVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_id", nullable = false)
    private Suggestion suggestion;

    @Column(name = "visitor_id", nullable = false)
    private String visitorId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
