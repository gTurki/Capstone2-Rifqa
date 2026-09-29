package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);

    List<Review> findReviewsByReviewedUserId(Integer reviewedUserId);

    List<Review> findReviewsByAgreementId(Integer agreementId);

    Review findReviewByAgreementIdAndReviewerId(Integer agreementId, Integer reviewerId);

    // return null when the user has no reviews
    @Query("select avg(r.rating) from Review r where r.reviewedUserId = ?1")
    Double findAverageRatingByUserId(Integer userId);
}