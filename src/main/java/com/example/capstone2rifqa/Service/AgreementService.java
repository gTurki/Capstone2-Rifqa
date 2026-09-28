package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.AgreementDTO;
import com.example.capstone2rifqa.Entity.Agreement;
import com.example.capstone2rifqa.Entity.AgreementStatus;
import com.example.capstone2rifqa.Entity.ListingRequest;
import com.example.capstone2rifqa.Entity.Match;
import com.example.capstone2rifqa.Entity.MatchStatus;
import com.example.capstone2rifqa.Entity.RequestStatus;
import com.example.capstone2rifqa.Entity.RoomListing;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Repository.AgreementRepository;
import com.example.capstone2rifqa.Repository.AgreementTermRepository;
import com.example.capstone2rifqa.Repository.ListingRequestRepository;
import com.example.capstone2rifqa.Repository.MatchRepository;
import com.example.capstone2rifqa.Repository.RoomListingRepository;
import com.example.capstone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;
    private final AgreementTermRepository agreementTermRepository;
    private final UserRepository userRepository;
    private final RoomListingRepository roomListingRepository;
    private final MatchRepository matchRepository;
    private final ListingRequestRepository listingRequestRepository;

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
        List<Agreement> agreements = agreementRepository.findAgreementsByUserId(userId);
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

    // The roommates come from the confirmed match and the rent is locked from the listing.
    // Only the dates come from the body.
    public Boolean addAgreement(Integer matchId, Integer listingId, Integer userId, AgreementDTO agreementDTO) {
        Match match = matchRepository.findMatchById(matchId);
        if (match == null) {
            throw new ApiException("Match not found with ID: " + matchId);
        }
        if (!match.getUserOneId().equals(userId) && !match.getUserTwoId().equals(userId)) {
            throw new ApiException("Only the users in this match can create an agreement");
        }
        if (match.getStatus() != MatchStatus.CONFIRMED) {
            throw new ApiException("An agreement can only be created for a confirmed match");
        }

        RoomListing listing = roomListingRepository.findRoomListingById(listingId);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + listingId);
        }

        // The renter must have agreed to one of the two users joining this listing
        if (!hasAcceptedRequest(listingId, match.getUserOneId()) && !hasAcceptedRequest(listingId, match.getUserTwoId())) {
            throw new ApiException("One of you must have an accepted request for this listing");
        }

        checkNoOpenAgreement(match.getUserOneId());
        checkNoOpenAgreement(match.getUserTwoId());

        validateDates(agreementDTO);

        Agreement agreement = new Agreement();
        agreement.setUserOneId(match.getUserOneId());
        agreement.setUserTwoId(match.getUserTwoId());
        agreement.setListingId(listingId);
        agreement.setMonthlyRent(listing.getRentAmount());
        agreement.setStartDate(agreementDTO.getStartDate());
        agreement.setEndDate(agreementDTO.getEndDate());
        agreement.setStatus(AgreementStatus.DRAFT);

        agreementRepository.save(agreement);
        return true;
    }

    // Only the dates can change, and only while it's a draft
    public Boolean updateAgreement(Integer id, Integer userId, AgreementDTO agreementDTO) {
        Agreement agreement = getAgreementForParticipant(id, userId);
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Only draft agreements can be edited");
        }

        validateDates(agreementDTO);

        agreement.setStartDate(agreementDTO.getStartDate());
        agreement.setEndDate(agreementDTO.getEndDate());
        agreementRepository.save(agreement);
        return true;
    }

    public Boolean activateAgreement(Integer id, Integer userId) {
        Agreement agreement = getAgreementForParticipant(id, userId);
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Only draft agreements can be activated");
        }

        // House rules are locked once active, so they must be written first
        if (agreementTermRepository.findAgreementTermsByAgreementId(id).isEmpty()) {
            throw new ApiException("Add at least one house rule before activating the agreement");
        }

        agreement.setStatus(AgreementStatus.ACTIVE);
        agreementRepository.save(agreement);
        return true;
    }

    public Boolean terminateAgreement(Integer id, Integer userId) {
        Agreement agreement = getAgreementForParticipant(id, userId);
        if (agreement.getStatus() != AgreementStatus.ACTIVE) {
            throw new ApiException("Only active agreements can be terminated");
        }

        agreement.setStatus(AgreementStatus.TERMINATED);
        agreementRepository.save(agreement);
        return true;
    }

    public Boolean deleteAgreement(Integer id, Integer userId) {
        Agreement agreement = getAgreementForParticipant(id, userId);
        if (agreement.getStatus() != AgreementStatus.DRAFT) {
            throw new ApiException("Only draft agreements can be deleted");
        }

        // Terms store the agreement's ID, so they must be removed first
        agreementTermRepository.deleteAll(agreementTermRepository.findAgreementTermsByAgreementId(id));
        agreementRepository.delete(agreement);
        return true;
    }

    // Shared by update, activate, terminate and delete: the agreement exists and this user is in it
    private Agreement getAgreementForParticipant(Integer agreementId, Integer userId) {
        Agreement agreement = agreementRepository.findAgreementById(agreementId);
        if (agreement == null) {
            throw new ApiException("Agreement not found with ID: " + agreementId);
        }
        if (!agreement.getUserOneId().equals(userId) && !agreement.getUserTwoId().equals(userId)) {
            throw new ApiException("Only the users in this agreement can make changes to it");
        }
        return agreement;
    }

    private boolean hasAcceptedRequest(Integer listingId, Integer userId) {
        ListingRequest request = listingRequestRepository.findListingRequestByListingIdAndRequesterId(listingId, userId);
        return request != null && request.getStatus() == RequestStatus.ACCEPTED;
    }

    // A person can only be in one draft or active agreement at a time
    private void checkNoOpenAgreement(Integer userId) {
        if (!agreementRepository.findAgreementsByUserIdAndStatusNot(userId, AgreementStatus.TERMINATED).isEmpty()) {
            throw new ApiException("User with ID " + userId + " already has a draft or active agreement");
        }
    }

    private void validateDates(AgreementDTO agreementDTO) {
        if (agreementDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new ApiException("Start date cannot be in the past");
        }
        if (!agreementDTO.getEndDate().isAfter(agreementDTO.getStartDate())) {
            throw new ApiException("End date must be after start date");
        }
    }
}