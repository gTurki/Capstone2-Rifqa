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
@Table(name = "review")
@Check(constraints = "rating >= 1 AND rating <= 5")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    @NotNull(message = "Agreement ID is required")
    @Column(name = "agreement_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer agreementId;

    @NotNull(message = "Reviewer ID is required")
    @Column(name = "reviewer_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer reviewerId;

    @NotNull(message = "Reviewed User ID is required")
    @Column(name = "reviewed_user_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer reviewedUserId;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot exceed 5")
    @Column(nullable = false, columnDefinition = "INT NOT NULL")
    private Integer rating;

    @Size(max = 500, message = "Comment cannot exceed 500 characters")
    @Column(columnDefinition = "VARCHAR(500)")
    private String comment;

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}