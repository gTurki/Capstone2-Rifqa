package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.Entity.Agreement;
import com.example.capstone2rifqa.Entity.AgreementStatus;
import com.example.capstone2rifqa.Entity.AgreementTerm;
import com.example.capstone2rifqa.Repository.AgreementRepository;
import com.example.capstone2rifqa.Repository.AgreementTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementTermService {

    private final AgreementTermRepository agreementTermRepository;
    private final AgreementRepository agreementRepository;

    public List<AgreementTerm> getAllAgreementTerms() {
        return agreementTermRepository.findAll();
    }

    public AgreementTerm getAgreementTermById(Integer id) {
        AgreementTerm term = agreementTermRepository.findAgreementTermById(id);
        if (term == null) {
            throw new ApiException("Agreement term not found with ID: " + id);
        }
        return term;
    }

    public List<AgreementTerm> getTermsByAgreementId(Integer agreementId) {
        Agreement agreement = agreementRepository.findAgreementById(agreementId);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + agreementId);
        }
        List<AgreementTerm> terms = agreementTermRepository.findAgreementTermsByAgreementId(agreementId);
        if (terms.isEmpty()) {
            throw new ApiException("This agreement has no terms yet");
        }
        return terms;
    }

    public Boolean addAgreementTerm(AgreementTerm term) {
        checkAgreementIsDraft(term.getAgreementId());

        agreementTermRepository.save(term);
        return true;
    }

    public Boolean updateAgreementTerm(Integer id, AgreementTerm updatedTerm) {
        AgreementTerm term = agreementTermRepository.findAgreementTermById(id);
        if (term == null) {
            throw new ApiException("Agreement term not found with ID: " + id);
        }
        checkAgreementIsDraft(term.getAgreementId());

        term.setTermText(updatedTerm.getTermText());
        agreementTermRepository.save(term);
        return true;
    }

    public Boolean deleteAgreementTerm(Integer id) {
        AgreementTerm term = agreementTermRepository.findAgreementTermById(id);
        if (term == null) {
            throw new ApiException("Agreement term not found with ID: " + id);
        }
        checkAgreementIsDraft(term.getAgreementId());

        agreementTermRepository.delete(term);
        return true;
    }

    // House rules are locked once the agreement becomes ACTIVE
    private void checkAgreementIsDraft(Integer agreementId) {
        Agreement agreement = agreementRepository.findAgreementById(agreementId);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + agreementId);
        }
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Terms can only be changed while the agreement is a draft");
        }
    }
}