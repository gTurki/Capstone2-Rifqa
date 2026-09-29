package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.RoomListingDTO;
import com.example.capstone2rifqa.Entity.Agreement;
import com.example.capstone2rifqa.Entity.Renter;
import com.example.capstone2rifqa.Entity.RoomListing;
import com.example.capstone2rifqa.Repository.AgreementRepository;
import com.example.capstone2rifqa.Repository.ListingRequestRepository;
import com.example.capstone2rifqa.Repository.RenterRepository;
import com.example.capstone2rifqa.Repository.RoomListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomListingService {

    private final RoomListingRepository roomListingRepository;
    private final RenterRepository renterRepository;
    private final ListingRequestRepository listingRequestRepository;
    private final AgreementRepository agreementRepository;

    public List<RoomListing> getAllRoomListings() {
        return roomListingRepository.findAll();
    }

    public RoomListing getRoomListingById(Integer id) {
        RoomListing roomListing = roomListingRepository.findRoomListingById(id);
        if (roomListing == null) {
            throw new ApiException("Room listing not found with ID: " + id);
        }
        return roomListing;
    }

    public List<RoomListing> getRoomListingsByRenterId(Integer renterId) {
        Renter renter = renterRepository.findRenterById(renterId);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + renterId);
        }
        List<RoomListing> listings = roomListingRepository.findRoomListingsByRenterId(renterId);
        if (listings.isEmpty()) {
            throw new ApiException("This renter has no listings");
        }
        return listings;
    }

    public List<RoomListing> getAvailableListingsByCity(String city) {
        List<RoomListing> listings = roomListingRepository.findRoomListingsByCityAndAvailable(city, true);
        if (listings.isEmpty()) {
            throw new ApiException("No available listings found in city: " + city);
        }
        return listings;
    }

    public List<RoomListing> getAvailableListingsByMaxRent(Double maxRent) {
        List<RoomListing> listings = roomListingRepository.findAvailableListingsByMaxRent(maxRent);
        if (listings.isEmpty()) {
            throw new ApiException("No available listings found with rent up to " + maxRent);
        }
        return listings;
    }

    public void addRoomListing(Integer renterId, RoomListingDTO roomListingDTO) {
        Renter renter = renterRepository.findRenterById(renterId);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + renterId);
        }

        RoomListing listing = new RoomListing();
        listing.setRenterId(renterId);
        listing.setTitle(roomListingDTO.getTitle());
        listing.setCity(roomListingDTO.getCity());
        listing.setDistrict(roomListingDTO.getDistrict());
        listing.setRentAmount(roomListingDTO.getRentAmount());
        listing.setDescription(roomListingDTO.getDescription());
        listing.setAvailable(true);

        roomListingRepository.save(listing);
    }

    public void updateRoomListing(Integer listingId, Integer renterId, RoomListingDTO roomListingDTO) {
        RoomListing listing = getListingForOwner(listingId, renterId);

        listing.setTitle(roomListingDTO.getTitle());
        listing.setCity(roomListingDTO.getCity());
        listing.setDistrict(roomListingDTO.getDistrict());
        listing.setRentAmount(roomListingDTO.getRentAmount());
        listing.setDescription(roomListingDTO.getDescription());

        roomListingRepository.save(listing);
    }

    public Boolean toggleListingAvailability(Integer listingId, Integer renterId) {
        RoomListing listing = getListingForOwner(listingId, renterId);

        listing.setAvailable(!listing.getAvailable());
        roomListingRepository.save(listing);
        return listing.getAvailable();
    }

    public void deleteRoomListing(Integer listingId, Integer renterId) {
        RoomListing listing = getListingForOwner(listingId, renterId);

        List<Agreement> agreements = agreementRepository.findAgreementsByListingId(listingId);
        if (!agreements.isEmpty()) {
            throw new ApiException("This listing has agreements and cannot be deleted. Mark it as not available instead");
        }

        listingRequestRepository.deleteAll(listingRequestRepository.findListingRequestsByListingId(listingId));
        roomListingRepository.delete(listing);
    }

    private RoomListing getListingForOwner(Integer listingId, Integer renterId) {
        RoomListing listing = roomListingRepository.findRoomListingById(listingId);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + listingId);
        }
        if (!listing.getRenterId().equals(renterId)) {
            throw new ApiException("Only the owner of this listing can make changes to it");
        }
        return listing;
    }
}