package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.DTO.ReviewDTO;
import com.example.capstone2rifqa.Service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllReviews() {
        return ResponseEntity.status(200).body(reviewService.getAllReviews());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(reviewService.getReviewById(id));
    }

    @GetMapping("/get-by-user/{userId}")
    public ResponseEntity<?> getReviewsForUser(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(reviewService.getReviewsForUser(userId));
    }

    @GetMapping("/get-by-agreement/{agreementId}")
    public ResponseEntity<?> getReviewsByAgreementId(@PathVariable Integer agreementId) {
        return ResponseEntity.status(200).body(reviewService.getReviewsByAgreementId(agreementId));
    }

    @GetMapping("/get-average-rating/{userId}")
    public ResponseEntity<?> getAverageRating(@PathVariable Integer userId) {
        return ResponseEntity.status(200).body(reviewService.getAverageRating(userId));
    }

    @PostMapping("/add/agreementid/{agreementId}/reviewerid/{reviewerId}")
    public ResponseEntity<?> addReview(@PathVariable Integer agreementId, @PathVariable Integer reviewerId, @RequestBody @Valid ReviewDTO reviewDTO) {
        reviewService.addReview(agreementId, reviewerId, reviewDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }

    @PutMapping("/update/reviewid/{id}/reviewerid/{reviewerId}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id, @PathVariable Integer reviewerId, @RequestBody @Valid ReviewDTO reviewDTO) {
        reviewService.updateReview(id, reviewerId, reviewDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
    }

    @DeleteMapping("/delete/reviewid/{id}/reviewerid/{reviewerId}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id, @PathVariable Integer reviewerId) {
        reviewService.deleteReview(id, reviewerId);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
    }
}