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
@Table(name = "renter")
public class Renter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    @Column(nullable = false, columnDefinition = "VARCHAR(25) NOT NULL")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(50) NOT NULL")
    private String email;

    @NotEmpty(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    @Column(nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
    private String password;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be 10 digits long")
    @Column(name = "phone_number", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
    private String phoneNumber;

    @Column(columnDefinition = "VARCHAR(50)")
    private String city;

    @Column(name = "is_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isVerified = false;

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}