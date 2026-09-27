package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.Entity.Agreement;
import com.example.capstone2rifqa.Entity.AgreementStatus;
import com.example.capstone2rifqa.Entity.Review;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Repository.AgreementRepository;
import com.example.capstone2rifqa.Repository.ReviewRepository;
import com.example.capstone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final AgreementRepository agreementRepository;
    private final UserRepository userRepository;

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Review getReviewById(Integer id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new ApiException("Review not found with ID: " + id);
        }
        return review;
    }

    public List<Review> getReviewsForUser(Integer userId) {
        checkUserExists(userId);
        List<Review> reviews = reviewRepository.findReviewsByReviewedUserId(userId);
        if (reviews.isEmpty()) {
            throw new ApiException("This user has no reviews yet");
        }
        return reviews;
    }

    public List<Review> getReviewsByAgreementId(Integer agreementId) {
        Agreement agreement = agreementRepository.findAgreementById(agreementId);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + agreementId);
        }
        List<Review> reviews = reviewRepository.findReviewsByAgreementId(agreementId);
        if (reviews.isEmpty()) {
            throw new ApiException("No reviews found for this agreement");
        }
        return reviews;
    }

    public Double getAverageRating(Integer userId) {
        checkUserExists(userId);

        List<Review> reviews = reviewRepository.findReviewsByReviewedUserId(userId);
        if (reviews.isEmpty()) {
            return 0.0;
        }

        double total = 0;
        for (Review review : reviews) {
            total += review.getRating();
        }
        return total / reviews.size();
    }

    public Boolean addReview(Review review) {
        Agreement agreement = agreementRepository.findAgreementById(review.getAgreementId());
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + review.getAgreementId());
        }
        if (agreement.getStatus() != AgreementStatus.TERMINATED) {
            throw new ApiException("Reviews can only be submitted after the agreement has ended");
        }

        Integer reviewerId = review.getReviewerId();
        Integer reviewedId = review.getReviewedUserId();

        if (reviewerId.equals(reviewedId)) {
            throw new ApiException("You cannot review yourself");
        }

        boolean reviewerInAgreement = reviewerId.equals(agreement.getUserOneId()) || reviewerId.equals(agreement.getUserTwoId());
        boolean reviewedInAgreement = reviewedId.equals(agreement.getUserOneId()) || reviewedId.equals(agreement.getUserTwoId());
        if (!reviewerInAgreement || !reviewedInAgreement) {
            throw new ApiException("Both users must be part of this agreement");
        }

        Review existing = reviewRepository.findReviewByAgreementIdAndReviewerId(review.getAgreementId(), reviewerId);
        if (existing != null) {
            throw new ApiException("You have already reviewed your roommate for this agreement");
        }

        reviewRepository.save(review);
        return true;
    }

    public Boolean updateReview(Integer id, Review updatedReview) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new ApiException("Review not found with ID: " + id);
        }

        review.setRating(updatedReview.getRating());
        review.setComment(updatedReview.getComment());

        reviewRepository.save(review);
        return true;
    }

    public Boolean deleteReview(Integer id) {
        Review review = reviewRepository.findReviewById(id);
        if (review == null) {
            throw new ApiException("Review not found with ID: " + id);
        }
        reviewRepository.delete(review);
        return true;
    }

    private void checkUserExists(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
    }
}