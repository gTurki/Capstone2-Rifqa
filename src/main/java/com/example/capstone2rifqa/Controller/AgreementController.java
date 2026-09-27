package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.Agreement;
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

    @PostMapping("/add")
    public ResponseEntity<?> addAgreement(@RequestBody @Valid Agreement agreement) {
        agreementService.addAgreement(agreement);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAgreement(@PathVariable Integer id, @RequestBody @Valid Agreement agreement) {
        agreementService.updateAgreement(id, agreement);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement updated successfully"));
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<?> activateAgreement(@PathVariable Integer id) {
        agreementService.activateAgreement(id);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement activated successfully"));
    }

    @PutMapping("/terminate/{id}")
    public ResponseEntity<?> terminateAgreement(@PathVariable Integer id) {
        agreementService.terminateAgreement(id);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement terminated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAgreement(@PathVariable Integer id) {
        agreementService.deleteAgreement(id);
        return ResponseEntity.status(200).body(new ApiResponse("Agreement deleted successfully"));
    }
}