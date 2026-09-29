package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.ListingRequestDTO;
import com.example.capstone2rifqa.Entity.ListingRequest;
import com.example.capstone2rifqa.Entity.Renter;
import com.example.capstone2rifqa.Entity.RequestStatus;
import com.example.capstone2rifqa.Entity.RoomListing;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Repository.ListingRequestRepository;
import com.example.capstone2rifqa.Repository.RenterRepository;
import com.example.capstone2rifqa.Repository.RoomListingRepository;
import com.example.capstone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListingRequestService {

    private final ListingRequestRepository listingRequestRepository;
    private final RoomListingRepository roomListingRepository;
    private final UserRepository userRepository;
    private final RenterRepository renterRepository;
    private final EmailService emailService;

    public List<ListingRequest> getAllListingRequests() {
        return listingRequestRepository.findAll();
    }

    public ListingRequest getListingRequestById(Integer id) {
        ListingRequest request = listingRequestRepository.findListingRequestById(id);
        if (request == null) {
            throw new ApiException("Listing request not found with ID: " + id);
        }
        return request;
    }

    public List<ListingRequest> getRequestsByListingId(Integer listingId) {
        checkListingExists(listingId);
        List<ListingRequest> requests = listingRequestRepository.findListingRequestsByListingId(listingId);
        if (requests.isEmpty()) {
            throw new ApiException("No requests found for this listing");
        }
        return requests;
    }

    public List<ListingRequest> getPendingRequestsByListingId(Integer listingId) {
        checkListingExists(listingId);
        List<ListingRequest> requests = listingRequestRepository.findListingRequestsByListingIdAndStatus(listingId, RequestStatus.PENDING);
        if (requests.isEmpty()) {
            throw new ApiException("No pending requests for this listing");
        }
        return requests;
    }

    public List<ListingRequest> getRequestsByRequesterId(Integer requesterId) {
        User user = userRepository.findUserById(requesterId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + requesterId);
        }
        List<ListingRequest> requests = listingRequestRepository.findListingRequestsByRequesterId(requesterId);
        if (requests.isEmpty()) {
            throw new ApiException("This user has not sent any requests");
        }
        return requests;
    }

    public void addListingRequest(Integer listingId, Integer userId, ListingRequestDTO listingRequestDTO) {
        RoomListing listing = roomListingRepository.findRoomListingById(listingId);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + listingId);
        }
        if (!listing.getAvailable()) {
            throw new ApiException("This listing is no longer available");
        }

        User requester = userRepository.findUserById(userId);
        if (requester == null) {
            throw new ApiException("User not found with ID: " + userId);
        }

        ListingRequest existing = listingRequestRepository.findListingRequestByListingIdAndRequesterId(listingId, userId);
        if (existing != null) {
            throw new ApiException("You have already sent a request for this listing");
        }

        ListingRequest request = new ListingRequest();
        request.setListingId(listingId);
        request.setRequesterId(userId);
        request.setMessage(listingRequestDTO.getMessage());
        request.setStatus(RequestStatus.PENDING);

        listingRequestRepository.save(request);
    }

    public void updateListingRequest(Integer id, Integer userId, ListingRequestDTO listingRequestDTO) {
        ListingRequest request = getRequestForRequester(id, userId);
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ApiException("Only pending requests can be edited");
        }

        request.setMessage(listingRequestDTO.getMessage());
        listingRequestRepository.save(request);
    }

    public void acceptRequest(Integer requestId, Integer renterId) {
        ListingRequest request = getPendingRequestForOwner(requestId, renterId);

        RoomListing listing = roomListingRepository.findRoomListingById(request.getListingId());
        if (!listing.getAvailable()) {
            throw new ApiException("This listing is no longer available");
        }

        request.setStatus(RequestStatus.ACCEPTED);
        listingRequestRepository.save(request);

        User requester = userRepository.findUserById(request.getRequesterId());
        Renter renter = renterRepository.findRenterById(renterId);
        if (requester != null) {
            emailService.sendEmail(
                    requester.getEmail(),
                    "Rifqa - Your room request was accepted",
                    "Hi " + requester.getName() + ",\n\n"
                            + "Good news! Your request to join \"" + listing.getTitle() + "\" in "
                            + listing.getDistrict() + ", " + listing.getCity() + " has been accepted.\n\n"
                            + "You can contact the renter, " + renter.getName() + ", at " + renter.getPhoneNumber() + ".\n\n"
                            + "The Rifqa Team"
            );
        }
    }

    public void rejectRequest(Integer requestId, Integer renterId) {
        ListingRequest request = getPendingRequestForOwner(requestId, renterId);

        request.setStatus(RequestStatus.REJECTED);
        listingRequestRepository.save(request);
    }

    //  just the user who sent the request can cancel it
    public void deleteListingRequest(Integer id, Integer userId) {
        ListingRequest request = getRequestForRequester(id, userId);

        listingRequestRepository.delete(request);
    }

    private ListingRequest getRequestForRequester(Integer requestId, Integer userId) {
        ListingRequest request = listingRequestRepository.findListingRequestById(requestId);
        if (request == null) {
            throw new ApiException("Listing request not found with ID: " + requestId);
        }
        if (!request.getRequesterId().equals(userId)) {
            throw new ApiException("Only the user who sent this request can make changes to it");
        }
        return request;
    }

    private ListingRequest getPendingRequestForOwner(Integer requestId, Integer renterId) {
        ListingRequest request = listingRequestRepository.findListingRequestById(requestId);
        if (request == null) {
            throw new ApiException("Listing request not found with ID: " + requestId);
        }

        RoomListing listing = roomListingRepository.findRoomListingById(request.getListingId());
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + request.getListingId());
        }
        if (!listing.getRenterId().equals(renterId)) {
            throw new ApiException("Only the owner of this listing can respond to its requests");
        }
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new ApiException("This request has already been " + request.getStatus());
        }
        return request;
    }

    private void checkListingExists(Integer listingId) {
        RoomListing listing = roomListingRepository.findRoomListingById(listingId);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + listingId);
        }
    }
}