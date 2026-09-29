package com.example.capstone2rifqa.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// used by add and update agreement endpoints. The users come from the match, the rent comes from the listing, and status is set by the server.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementDTO {

    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
}