package com.example.castone2rifqa.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "report")
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    @NotNull(message = "Reporter ID is required")
    @Column(name = "reporter_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer reporterId;

    @NotNull(message = "Reported User ID is required")
    @Column(name = "reported_user_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer reportedUserId;

    @NotEmpty(message = "Reason is required")
    @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
    @Column(nullable = false, columnDefinition = "VARCHAR(500) NOT NULL")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}