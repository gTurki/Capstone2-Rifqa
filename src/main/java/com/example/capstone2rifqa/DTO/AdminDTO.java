package com.example.capstone2rifqa.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// used by addAdmin endpoint. The id and creation date are set automatically, so an existing admin cannot be overwritten in the body.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDTO {

    @NotEmpty(message = "Name is required")
    @Size(min = 3, max = 25, message = "Name must be between 3 and 25 characters")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Email must be valid")
    @Size(max = 50, message = "Email cannot exceed 50 characters")
    private String email;

    @NotEmpty(message = "Password is required")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, one number, and one special character"
    )
    private String password;
}