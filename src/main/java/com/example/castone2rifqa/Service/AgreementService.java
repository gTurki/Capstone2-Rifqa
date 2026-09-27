package com.example.castone2rifqa.Service;

import com.example.castone2rifqa.Api.ApiException;
import com.example.castone2rifqa.Entity.Agreement;
import com.example.castone2rifqa.Entity.AgreementStatus;
import com.example.castone2rifqa.Entity.RoomListing;
import com.example.castone2rifqa.Entity.User;
import com.example.castone2rifqa.Repository.AgreementRepository;
import com.example.castone2rifqa.Repository.AgreementTermRepository;
import com.example.castone2rifqa.Repository.RoomListingRepository;
import com.example.castone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementTermRepository agreementTermRepository;
    private final UserRepository userRepository;
    private final RoomListingRepository roomListingRepository;

    public List<Agreement> getAllAgreements() {
        return agreementRepository.findAll();
    }

    public Agreement getAgreementById(Integer id) {
        Agreement agreement = agreementRepository.findAgreementById(id);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + id);
        }
        return agreement;
    }

    public List<Agreement> getAgreementsByUserId(Integer userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
        List<Agreement> agreements = agreementRepository.findAgreementsByUserOneIdOrUserTwoId(userId, userId);
        if (agreements.isEmpty()) {
            throw new ApiException("This user has no agreements");
        }
        return agreements;
    }

    public List<Agreement> getAgreementsByListingId(Integer listingId) {
        RoomListing listing = roomListingRepository.findRoomListingById(listingId);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + listingId);
        }
        List<Agreement> agreements = agreementRepository.findAgreementsByListingId(listingId);
        if (agreements.isEmpty()) {
            throw new ApiException("No agreements found for this listing");
        }
        return agreements;
    }

    public Boolean addAgreement(Agreement agreement) {
        validateAgreementDetails(agreement);

        agreement.setStatus(AgreementStatus.DRAFT);
        agreementRepository.save(agreement);
        return true;
    }

    public Boolean updateAgreement(Integer id, Agreement updatedAgreement) {
        Agreement agreement = agreementRepository.findAgreementById(id);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + id);
        }
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Only draft agreements can be edited");
        }

        validateAgreementDetails(updatedAgreement);

        agreement.setUserOneId(updatedAgreement.getUserOneId());
        agreement.setUserTwoId(updatedAgreement.getUserTwoId());
        agreement.setListingId(updatedAgreement.getListingId());
        agreement.setMonthlyRent(updatedAgreement.getMonthlyRent());
        agreement.setStartDate(updatedAgreement.getStartDate());
        agreement.setEndDate(updatedAgreement.getEndDate());

        agreementRepository.save(agreement);
        return true;
    }

    public Boolean activateAgreement(Integer id) {
        Agreement agreement = agreementRepository.findAgreementById(id);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + id);
        }
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Only draft agreements can be activated");
        }

        agreement.setStatus(AgreementStatus.ACTIVE);
        agreementRepository.save(agreement);
        return true;
    }

    public Boolean terminateAgreement(Integer id) {
        Agreement agreement = agreementRepository.findAgreementById(id);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + id);
        }
        if (agreement.getStatus() != AgreementStatus.ACTIVE) {
            throw new ApiException("Only active agreements can be terminated");
        }

        agreement.setStatus(AgreementStatus.TERMINATED);
        agreementRepository.save(agreement);
        return true;
    }

    public Boolean deleteAgreement(Integer id) {
        Agreement agreement = agreementRepository.findAgreementById(id);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + id);
        }
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Only draft agreements can be deleted");
        }

        // Terms reference the agreement (FK), so they must be removed first
        agreementTermRepository.deleteAll(agreementTermRepository.findAgreementTermsByAgreementId(id));
        agreementRepository.delete(agreement);
        return true;
    }

    private void validateAgreementDetails(Agreement agreement) {
        if (agreement.getUserOneId().equals(agreement.getUserTwoId())) {
            throw new ApiException("An agreement must be between two different users");
        }

        User userOne = userRepository.findUserById(agreement.getUserOneId());
        if (userOne == null) {
            throw new ApiException("User not found with ID: " + agreement.getUserOneId());
        }
        User userTwo = userRepository.findUserById(agreement.getUserTwoId());
        if (userTwo == null) {
            throw new ApiException("User not found with ID: " + agreement.getUserTwoId());
        }

        RoomListing listing = roomListingRepository.findRoomListingById(agreement.getListingId());
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + agreement.getListingId());
        }

        if (!agreement.getEndDate().isAfter(agreement.getStartDate())) {
            throw new ApiException("End date must be after start date");
        }
    }
}