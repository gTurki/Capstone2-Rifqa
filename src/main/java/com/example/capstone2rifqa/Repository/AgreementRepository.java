package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.Agreement;
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
}