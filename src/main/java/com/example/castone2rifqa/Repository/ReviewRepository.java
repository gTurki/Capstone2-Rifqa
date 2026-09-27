package com.example.castone2rifqa.Repository;

import com.example.castone2rifqa.Entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    Review findReviewById(Integer id);

    List<Review> findReviewsByReviewedUserId(Integer reviewedUserId);

    List<Review> findReviewsByAgreementId(Integer agreementId);

    Review findReviewByAgreementIdAndReviewerId(Integer agreementId, Integer reviewerId);
}