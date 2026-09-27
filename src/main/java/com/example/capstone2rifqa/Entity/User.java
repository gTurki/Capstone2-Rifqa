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
@Table(name = "user")
@Check(constraints = "age >= 18 AND age <= 100 AND cleanliness_level >= 1 AND cleanliness_level <= 5 AND budget >= 0")
public class User {

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

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be valid")
    @Column(nullable = false, columnDefinition = "INT NOT NULL")
    private Integer age;

    @NotEmpty(message = "Gender is required")
    @Column(nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
    private String gender;

    @NotEmpty(message = "City is required")
    @Column(nullable = false, columnDefinition = "VARCHAR(50) NOT NULL")
    private String city;

    @Column(columnDefinition = "VARCHAR(50)")
    private String occupation;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    @Column(columnDefinition = "VARCHAR(500)")
    private String bio;

    @NotNull(message = "Budget is required")
    @PositiveOrZero(message = "Budget must be zero or positive")
    @Column(nullable = false, columnDefinition = "DOUBLE NOT NULL")
    private Double budget;

    @NotNull(message = "Smoker status is required")
    @Column(nullable = false, columnDefinition = "BOOLEAN NOT NULL")
    private Boolean smoker;

    @NotNull(message = "Pet policy preference is required")
    @Column(name = "has_pets", nullable = false, columnDefinition = "BOOLEAN NOT NULL")
    private Boolean hasPets;

    @NotNull(message = "Visitor preference is required")
    @Column(name = "allows_visitors", nullable = false, columnDefinition = "BOOLEAN NOT NULL")
    private Boolean allowsVisitors;

    @NotNull(message = "Cleanliness level is required")
    @Min(value = 1, message = "Cleanliness level must be at least 1")
    @Max(value = 5, message = "Cleanliness level cannot exceed 5")
    @Column(name = "cleanliness_level", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer cleanlinessLevel;

    @NotNull(message = "Sleep schedule is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "sleep_schedule", nullable = false, columnDefinition = "VARCHAR(20) NOT NULL")
    private SleepSchedule sleepSchedule;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'LOOKING'")
    private UserStatus status = UserStatus.LOOKING;

    @Column(name = "is_verified", nullable = false, columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean isVerified = false;

    @Column(name = "created_at", updatable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}