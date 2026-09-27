package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.AgreementTerm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AgreementTermRepository extends JpaRepository<AgreementTerm, Integer> {

    AgreementTerm findAgreementTermById(Integer id);

    List<AgreementTerm> findAgreementTermsByAgreementId(Integer agreementId);
}