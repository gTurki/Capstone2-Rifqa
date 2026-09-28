package com.example.capstone2rifqa.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchSuggestion {

    // ID of the saved SUGGESTED match, used by send-request
    private Integer matchId;

    private Integer userId;

    private Integer suggestedUserId;

    private String suggestedUserName;

    private Double compatibilityScore;

    private String aiReasoning;
}