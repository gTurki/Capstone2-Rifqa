package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementRepository extends JpaRepository<Agreement, Integer> {

    Agreement findAgreementById(Integer id);

    List<Agreement> findAgreementsByUserOneIdOrUserTwoId(Integer userOneId, Integer userTwoId);

    List<Agreement> findAgreementsByListingId(Integer listingId);
}