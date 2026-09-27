package com.example.castone2rifqa.Entity;

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
@Table(name = "room_listing")
@Check(constraints = "rent_amount >= 0")
public class RoomListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    @NotNull(message = "Renter ID is required")
    @Column(name = "renter_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer renterId;

    @NotEmpty(message = "Title is required")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    @Column(nullable = false, columnDefinition = "VARCHAR(100) NOT NULL")
    private String title;

    @NotEmpty(message = "City is required")
    @Column(nullable = false, columnDefinition = "VARCHAR(50) NOT NULL")
    private String city;

    @NotEmpty(message = "District is required")
    @Column(nullable = false, columnDefinition = "VARCHAR(50) NOT NULL")
    private String district;

    @NotNull(message = "Rent amount is required")
    @PositiveOrZero(message = "Rent amount must be zero or positive")
    @Column(name = "rent_amount", nullable = false, columnDefinition = "DOUBLE NOT NULL")
    private Double rentAmount;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    @Column(columnDefinition = "VARCHAR(1000)")
    private String description;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean available = true;

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}