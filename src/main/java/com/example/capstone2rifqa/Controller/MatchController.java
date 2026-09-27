package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.Match;
import com.example.capstone2rifqa.Service.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllMatches() {
        return ResponseEntity.status(200).body(matchService.getAllMatches());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getMatchById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(matchService.getMatchById(id));
    }

    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<?> getMatchesByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(matchService.getMatchesByUserId(userId));
    }

    // AI suggestion only, nothing is saved
    @GetMapping("/suggest/{userId}")
    public ResponseEntity<?> suggestMatch(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(matchService.suggestMatch(userId));
    }

    // Saves the match after the user approves the suggestion
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmMatch(@RequestBody @Valid Match match) {
        matchService.confirmMatch(match);
        return ResponseEntity.status(200).body(new ApiResponse("Match confirmed successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMatch(@PathVariable Integer id) {
        matchService.deleteMatch(id);
        return ResponseEntity.status(200).body(new ApiResponse("Match deleted successfully"));
    }
}