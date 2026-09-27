package com.example.capstone2rifqa.Entity;

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
@Table(name = "admin")
public class Admin {

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

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}