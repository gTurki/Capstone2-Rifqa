package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.AgreementTermDTO;
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

    // The agreement comes from the path; only the text comes from the body
    public Boolean addAgreementTerm(Integer agreementId, Integer userId, AgreementTermDTO agreementTermDTO) {
        getDraftAgreementForParticipant(agreementId, userId);

        AgreementTerm term = new AgreementTerm();
        term.setAgreementId(agreementId);
        term.setTermText(agreementTermDTO.getTermText());

        agreementTermRepository.save(term);
        return true;
    }

    public Boolean updateAgreementTerm(Integer id, Integer userId, AgreementTermDTO agreementTermDTO) {
        AgreementTerm term = getTermForParticipant(id, userId);

        term.setTermText(agreementTermDTO.getTermText());
        agreementTermRepository.save(term);
        return true;
    }

    public Boolean deleteAgreementTerm(Integer id, Integer userId) {
        AgreementTerm term = getTermForParticipant(id, userId);

        agreementTermRepository.delete(term);
        return true;
    }

    // Shared by update and delete: the term exists and its agreement passes the checks below
    private AgreementTerm getTermForParticipant(Integer termId, Integer userId) {
        AgreementTerm term = agreementTermRepository.findAgreementTermById(termId);
        if (term == null) {
            throw new ApiException("Agreement term not found with ID: " + termId);
        }
        getDraftAgreementForParticipant(term.getAgreementId(), userId);
        return term;
    }

    // Only the two roommates can write house rules, and they are locked once the agreement becomes ACTIVE
    private Agreement getDraftAgreementForParticipant(Integer agreementId, Integer userId) {
        Agreement agreement = agreementRepository.findAgreementById(agreementId);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + agreementId);
        }
        if (!agreement.getUserOneId().equals(userId) && !agreement.getUserTwoId().equals(userId)) {
            throw new ApiException("Only the users in this agreement can change its house rules");
        }
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Terms can only be changed while the agreement is a draft");
        }
        return agreement;
    }
}