package com.example.capstone2rifqa.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "agreement_term")
public class AgreementTerm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    private Integer id;

    @NotNull(message = "Agreement ID is required")
    @Column(name = "agreement_id", nullable = false, columnDefinition = "INT NOT NULL")
    private Integer agreementId;

    @NotEmpty(message = "Term text is required")
    @Size(min = 3, max = 255, message = "Term text must be between 3 and 255 characters")
    @Column(name = "term_text", nullable = false, columnDefinition = "VARCHAR(255) NOT NULL")
    private String termText;
}