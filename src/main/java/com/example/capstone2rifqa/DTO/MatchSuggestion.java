package com.example.capstone2rifqa.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchSuggestion {

    private Integer userId;

    private Integer suggestedUserId;

    private String suggestedUserName;

    private Double compatibilityScore;

    private String aiReasoning;
}