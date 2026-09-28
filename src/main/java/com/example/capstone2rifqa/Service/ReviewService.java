package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.ReviewDTO;
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

    // Rounded to one decimal, e.g. 4.3. Returns 0.0 when the user has no reviews (real ratings are 1-5).
    public Double getAverageRating(Integer userId) {
        checkUserExists(userId);

        Double average = reviewRepository.findAverageRatingByUserId(userId);
        if (average == null) {
            return 0.0;
        }
        return Math.round(average * 10) / 10.0;
    }

    // The reviewed user is always the other roommate in the agreement, so the client never sends it
    public Boolean addReview(Integer agreementId, Integer reviewerId, ReviewDTO reviewDTO) {
        Agreement agreement = agreementRepository.findAgreementById(agreementId);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + agreementId);
        }

        Integer reviewedUserId;
        if (reviewerId.equals(agreement.getUserOneId())) {
            reviewedUserId = agreement.getUserTwoId();
        } else if (reviewerId.equals(agreement.getUserTwoId())) {
            reviewedUserId = agreement.getUserOneId();
        } else {
            throw new ApiException("Only the users in this agreement can review it");
        }

        if (agreement.getStatus() != AgreementStatus.TERMINATED) {
            throw new ApiException("Reviews can only be submitted after the agreement has ended");
        }

        Review existing = reviewRepository.findReviewByAgreementIdAndReviewerId(agreementId, reviewerId);
        if (existing != null) {
            throw new ApiException("You have already reviewed your roommate for this agreement");
        }

        Review review = new Review();
        review.setAgreementId(agreementId);
        review.setReviewerId(reviewerId);
        review.setReviewedUserId(reviewedUserId);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        reviewRepository.save(review);
        return true;
    }

    public Boolean updateReview(Integer id, Integer reviewerId, ReviewDTO reviewDTO) {
        Review review = getReviewForAuthor(id, reviewerId);

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        reviewRepository.save(review);
        return true;
    }

    public Boolean deleteReview(Integer id, Integer reviewerId) {
        Review review = getReviewForAuthor(id, reviewerId);

        reviewRepository.delete(review);
        return true;
    }

    // Shared by update and delete: the review exists and was written by this user
    private Review getReviewForAuthor(Integer reviewId, Integer reviewerId) {
        Review review = reviewRepository.findReviewById(reviewId);
        if (review == null) {
            throw new ApiException("Review not found with ID: " + reviewId);
        }
        if (!review.getReviewerId().equals(reviewerId)) {
            throw new ApiException("Only the author of this review can make changes to it");
        }
        return review;
    }

    private void checkUserExists(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
    }
}