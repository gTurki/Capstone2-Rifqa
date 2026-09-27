package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.AgreementTerm;
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

    @PostMapping("/add")
    public ResponseEntity<?> addAgreementTerm(@RequestBody @Valid AgreementTerm agreementTerm) {
        agreementTermService.addAgreementTerm(agreementTerm);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement term added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAgreementTerm(@PathVariable Integer id, @RequestBody @Valid AgreementTerm agreementTerm) {
        agreementTermService.updateAgreementTerm(id, agreementTerm);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement term updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAgreementTerm(@PathVariable Integer id) {
        agreementTermService.deleteAgreementTerm(id);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement term deleted successfully"));
    }
}