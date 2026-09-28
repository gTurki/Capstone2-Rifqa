package com.example.capstone2rifqa.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "`match`")
@Check(constraints = "compatibility_score >= 0 AND compatibility_score <= 100")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    // The user who asked for the suggestion and sends the request
    @NotNull(message = "User One ID is required")
    @Column(name = "user_one_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer userOneId;

    // The suggested user who receives the request
    @NotNull(message = "User Two ID is required")
    @Column(name = "user_two_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer userTwoId;

    @NotNull(message = "Compatibility score is required")
    @Min(value = 0, message = "Compatibility score must be at least 0")
    @Max(value = 100, message = "Compatibility score cannot exceed 100")
    @Column(name = "compatibility_score", nullable = false, columnDefinition = "DOUBLE NOT NULL")
    private Double compatibilityScore;

    @Size(max = 1000, message = "AI reasoning cannot exceed 1000 characters")
    @Column(name = "ai_reasoning", columnDefinition = "VARCHAR(1000)")
    private String aiReasoning;

    // Set by the service: SUGGESTED -> PENDING -> CONFIRMED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
    private MatchStatus status;

    @Column(name = "matched_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime matchedAt;

    @PrePersist
    protected void onCreate() {
        this.matchedAt = LocalDateTime.now();
    }
}