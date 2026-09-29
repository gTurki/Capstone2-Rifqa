package com.example.capstone2rifqa.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// used by add and update report endpoints. both users come from the path, and status is changed only by an admin.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDTO {

    @NotEmpty(message = "Reason is required")
    @Size(min = 5, max = 500, message = "Reason must be between 5 and 500 characters")
    private String reason;
}