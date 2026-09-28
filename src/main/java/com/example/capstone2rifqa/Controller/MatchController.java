package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Service.MatchService;
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

    // Pending requests this user has received
    @GetMapping("/get-incoming/{userId}")
    public ResponseEntity<?> getIncomingRequests(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(matchService.getIncomingRequests(userId));
    }

    // AI picks a candidate and saves it as SUGGESTED
    @PostMapping("/suggest/{userId}")
    public ResponseEntity<?> suggestMatch(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(matchService.suggestMatch(userId));
    }

    @PutMapping("/send-request/matchid/{matchId}/userid/{userId}")
    public ResponseEntity<?> sendRequest(@PathVariable Integer matchId, @PathVariable Integer userId) {
        matchService.sendRequest(matchId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Match request sent successfully"));
    }

    @PutMapping("/accept/matchid/{matchId}/userid/{userId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer matchId, @PathVariable Integer userId) {
        matchService.acceptRequest(matchId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Match request accepted successfully"));
    }

    @PutMapping("/decline/matchid/{matchId}/userid/{userId}")
    public ResponseEntity<?> declineRequest(@PathVariable Integer matchId, @PathVariable Integer userId) {
        matchService.declineRequest(matchId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Match request declined successfully"));
    }

    @DeleteMapping("/delete/matchid/{matchId}/userid/{userId}")
    public ResponseEntity<?> deleteMatch(@PathVariable Integer matchId, @PathVariable Integer userId) {
        matchService.deleteMatch(matchId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Match deleted successfully"));
    }
}