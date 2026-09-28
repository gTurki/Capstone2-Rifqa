package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.DTO.AgreementDTO;
import com.example.capstone2rifqa.Service.AgreementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agreement")
@RequiredArgsConstructor
public class AgreementController {

    private final AgreementService agreementService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllAgreements() {
        return ResponseEntity.status(200).body(agreementService.getAllAgreements());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAgreementById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(agreementService.getAgreementById(id));
    }

    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<?> getAgreementsByUserId(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(agreementService.getAgreementsByUserId(userId));
    }

    @GetMapping("/get-by-listing/{listingId}")
    public ResponseEntity<?> getAgreementsByListingId(@PathVariable Integer listingId) {
        return ResponseEntity.status(200).body(agreementService.getAgreementsByListingId(listingId));
    }

    @PostMapping("/add/matchid/{matchId}/listingid/{listingId}/userid/{userId}")
    public ResponseEntity<?> addAgreement(@PathVariable Integer matchId, @PathVariable Integer listingId, @PathVariable Integer userId, @RequestBody @Valid AgreementDTO agreementDTO) {
        agreementService.addAgreement(matchId, listingId, userId, agreementDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement added successfully"));
    }

    @PutMapping("/update/agreementid/{id}/userid/{userId}")
    public ResponseEntity<?> updateAgreement(@PathVariable Integer id, @PathVariable Integer userId, @RequestBody @Valid AgreementDTO agreementDTO) {
        agreementService.updateAgreement(id, userId, agreementDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement updated successfully"));
    }

    @PutMapping("/activate/agreementid/{id}/userid/{userId}")
    public ResponseEntity<?> activateAgreement(@PathVariable Integer id, @PathVariable Integer userId) {
        agreementService.activateAgreement(id, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement activated successfully"));
    }

    @PutMapping("/terminate/agreementid/{id}/userid/{userId}")
    public ResponseEntity<?> terminateAgreement(@PathVariable Integer id, @PathVariable Integer userId) {
        agreementService.terminateAgreement(id, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement terminated successfully"));
    }

    @DeleteMapping("/delete/agreementid/{id}/userid/{userId}")
    public ResponseEntity<?> deleteAgreement(@PathVariable Integer id, @PathVariable Integer userId) {
        agreementService.deleteAgreement(id, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement deleted successfully"));
    }
}