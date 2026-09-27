package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.MatchSuggestion;
import com.example.capstone2rifqa.Entity.User;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private static final String SYSTEM_PROMPT =
            "You are a roommate compatibility assistant. Compare the target user with each candidate "
                    + "using budget, sleep schedule, cleanliness level, smoking, pets, visitors, age and occupation. "
                    + "Pick the single most compatible candidate. Respond ONLY with a JSON object in this exact format: "
                    + "{\"matchedUserId\": <candidate id>, \"compatibilityScore\": <number from 0 to 100>, "
                    + "\"reasoning\": \"<2-3 short sentences explaining the match>\"}";

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${ai.api.url}")
    private String apiUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    @Value("${ai.api.model}")
    private String model;

    public MatchSuggestion suggestBestMatch(User target, List<User> candidates) {
        String aiContent = callAi(buildPrompt(target, candidates));

        try {
            JsonNode result = objectMapper.readTree(aiContent);

            Integer matchedUserId = result.path("matchedUserId").asInt();
            Double score = result.path("compatibilityScore").asDouble();
            String reasoning = result.path("reasoning").asText("");

            // Keep values inside the DB limits (score 0-100, ai_reasoning VARCHAR(1000))
            score = Math.max(0.0, Math.min(100.0, score));
            if (reasoning.length() > 1000) {
                reasoning = reasoning.substring(0, 1000);
            }

            MatchSuggestion suggestion = new MatchSuggestion();
            suggestion.setSuggestedUserId(matchedUserId);
            suggestion.setCompatibilityScore(score);
            suggestion.setAiReasoning(reasoning);
            return suggestion;
        } catch (JacksonException e) {
            throw new ApiException("Could not understand the AI response, please try again");
        }
    }

    private String callAi(String prompt) {
        Map<String, Object> body = new HashMap<>();
        body.put("model", model);
        body.put("response_format", Map.of("type", "json_object"));
        body.put("messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", prompt)
        ));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        try {
            String response = restTemplate.postForObject(apiUrl, new HttpEntity<>(body, headers), String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").path(0).path("message").path("content").asText();
        } catch (Exception e) {
            System.out.println("AI error: " + e.getMessage());
            throw new ApiException("AI matching service is currently unavailable");
        }
    }

    private String buildPrompt(User target, List<User> candidates) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Target user:\n").append(describe(target)).append("\n\nCandidates:\n");
        for (User candidate : candidates) {
            prompt.append(describe(candidate)).append("\n");
        }
        return prompt.toString();
    }

    // Only lifestyle fields are sent. Name, email, phone and password never leave the server.
    private String describe(User user) {
        return "id=" + user.getId()
                + ", age=" + user.getAge()
                + ", occupation=" + user.getOccupation()
                + ", budget=" + user.getBudget()
                + ", smoker=" + user.getSmoker()
                + ", hasPets=" + user.getHasPets()
                + ", allowsVisitors=" + user.getAllowsVisitors()
                + ", cleanlinessLevel=" + user.getCleanlinessLevel() + "/5"
                + ", sleepSchedule=" + user.getSleepSchedule()
                + ", bio=\"" + (user.getBio() == null ? "" : user.getBio()) + "\"";
    }
}