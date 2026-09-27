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
@Table(name = "listing_request")
public class ListingRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    @NotNull(message = "Listing ID is required")
    @Column(name = "listing_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer listingId;

    @NotNull(message = "Requester ID is required")
    @Column(name = "requester_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer requesterId;

    @Size(max = 500, message = "Message cannot exceed 500 characters")
    @Column(columnDefinition = "VARCHAR(500)")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'PENDING'")
    private RequestStatus status = RequestStatus.PENDING;

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}