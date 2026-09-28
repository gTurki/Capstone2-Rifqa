package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.DTO.AgreementTermDTO;
import com.example.capstone2rifqa.Service.AgreementTermService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/agreement-term")
@RequiredArgsConstructor
public class AgreementTermController {

    private final AgreementTermService agreementTermService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllAgreementTerms() {
        return ResponseEntity.status(200).body(agreementTermService.getAllAgreementTerms());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAgreementTermById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(agreementTermService.getAgreementTermById(id));
    }

    @GetMapping("/get-by-agreement/{agreementId}")
    public ResponseEntity<?> getTermsByAgreementId(@PathVariable Integer agreementId) {
        return ResponseEntity.status(200).body(agreementTermService.getTermsByAgreementId(agreementId));
    }

    @PostMapping("/add/agreementid/{agreementId}/userid/{userId}")
    public ResponseEntity<?> addAgreementTerm(@PathVariable Integer agreementId, @PathVariable Integer userId, @RequestBody @Valid AgreementTermDTO agreementTermDTO) {
        agreementTermService.addAgreementTerm(agreementId, userId, agreementTermDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement term added successfully"));
    }

    @PutMapping("/update/termid/{id}/userid/{userId}")
    public ResponseEntity<?> updateAgreementTerm(@PathVariable Integer id, @PathVariable Integer userId, @RequestBody @Valid AgreementTermDTO agreementTermDTO) {
        agreementTermService.updateAgreementTerm(id, userId, agreementTermDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement term updated successfully"));
    }

    @DeleteMapping("/delete/termid/{id}/userid/{userId}")
    public ResponseEntity<?> deleteAgreementTerm(@PathVariable Integer id, @PathVariable Integer userId) {
        agreementTermService.deleteAgreementTerm(id, userId);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement term deleted successfully"));
    }
}