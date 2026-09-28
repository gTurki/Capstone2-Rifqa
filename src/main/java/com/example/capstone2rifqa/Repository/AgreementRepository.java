package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.Agreement;
import com.example.capstone2rifqa.Entity.AgreementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Integer> {

    Agreement findAgreementById(Integer id);

    List<Agreement> findAgreementsByListingId(Integer listingId);

    @Query("select a from Agreement a where a.userOneId = ?1 or a.userTwoId = ?1")
    List<Agreement> findAgreementsByUserId(Integer userId);

    // Used with TERMINATED to find the user's draft or active agreements
    @Query("select a from Agreement a where (a.userOneId = ?1 or a.userTwoId = ?1) and a.status <> ?2")
    List<Agreement> findAgreementsByUserIdAndStatusNot(Integer userId, AgreementStatus status);

    // A pair can be stored as (A, B) or (B, A), so check both orders
    @Query("select a from Agreement a where ((a.userOneId = ?1 and a.userTwoId = ?2) or (a.userOneId = ?2 and a.userTwoId = ?1)) and a.status <> ?3")
    List<Agreement> findAgreementsBetweenUsersByStatusNot(Integer userOneId, Integer userTwoId, AgreementStatus status);
}