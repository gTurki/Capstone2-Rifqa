package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.Review;
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

    // Reviews the user has received
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
        Double average = reviewService.getAverageRating(userId);
        String rating = String.format("%.1f", average);
        return ResponseEntity.status(200).body(new ApiResponse("Average rating for user " + userId + " is " + rating + " out of 5"));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addReview(@RequestBody @Valid Review review) {
        reviewService.addReview(review);
        return ResponseEntity.status(200).body(new ApiResponse("Review added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Integer id, @RequestBody @Valid Review review) {
        reviewService.updateReview(id, review);
        return ResponseEntity.status(200).body(new ApiResponse("Review updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Integer id) {
        reviewService.deleteReview(id);
        return ResponseEntity.status(200).body(new ApiResponse("Review deleted successfully"));
    }
}