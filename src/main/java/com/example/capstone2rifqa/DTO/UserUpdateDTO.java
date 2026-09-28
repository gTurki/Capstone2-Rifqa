package com.example.capstone2rifqa.DTO;

import com.example.capstone2rifqa.Entity.SleepSchedule;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Profile fields only. Password has its own endpoint, and status is changed through update-status or the match flow.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateDTO {

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^05\\d{8}$", message = "Phone number must start with 05 and be 10 digits long")
    private String phoneNumber;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 100, message = "Age must be valid")
    private Integer age;

    @NotEmpty(message = "Gender is required")
    private String gender;

    @NotEmpty(message = "City is required")
    private String city;

    private String occupation;

    @Size(max = 500, message = "Bio cannot exceed 500 characters")
    private String bio;

    @NotNull(message = "Budget is required")
    @PositiveOrZero(message = "Budget must be zero or positive")
    private Double budget;

    @NotNull(message = "Smoker status is required")
    private Boolean smoker;

    @NotNull(message = "Pet policy preference is required")
    private Boolean hasPets;

    @NotNull(message = "Visitor preference is required")
    private Boolean allowsVisitors;

    @NotNull(message = "Cleanliness level is required")
    @Min(value = 1, message = "Cleanliness level must be at least 1")
    @Max(value = 5, message = "Cleanliness level cannot exceed 5")
    private Integer cleanlinessLevel;

    @NotNull(message = "Sleep schedule is required")
    private SleepSchedule sleepSchedule;
}