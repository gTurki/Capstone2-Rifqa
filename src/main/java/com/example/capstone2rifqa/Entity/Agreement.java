package com.example.capstone2rifqa.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "agreement")
@Check(constraints = "monthly_rent >= 0 AND end_date >= start_date")
public class Agreement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    @NotNull(message = "User One ID is required")
    @Column(name = "user_one_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer userOneId;

    @NotNull(message = "User Two ID is required")
    @Column(name = "user_two_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer userTwoId;

    @NotNull(message = "Listing ID is required")
    @Column(name = "listing_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer listingId;

    @NotNull(message = "Monthly rent is required")
    @PositiveOrZero(message = "Monthly rent must be zero or positive")
    @Column(name = "monthly_rent", nullable = false, columnDefinition = "DOUBLE NOT NULL")
    private Double monthlyRent;

    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false, columnDefinition = "DATE NOT NULL")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false, columnDefinition = "DATE NOT NULL")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'DRAFT'")
    private AgreementStatus status = AgreementStatus.DRAFT;

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}