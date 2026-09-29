package com.example.capstone2rifqa.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// used by add and update agreement terms endpoints. The agreement comes from the path.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgreementTermDTO {

    @NotEmpty(message = "Term text is required")
    @Size(min = 3, max = 255, message = "Term text must be between 3 and 255 characters")
    private String termText;
}