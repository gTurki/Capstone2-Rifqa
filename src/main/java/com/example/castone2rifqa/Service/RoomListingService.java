package com.example.castone2rifqa.Service;

import com.example.castone2rifqa.Api.ApiException;
import com.example.castone2rifqa.Entity.Renter;
import com.example.castone2rifqa.Entity.RoomListing;
import com.example.castone2rifqa.Repository.RenterRepository;
import com.example.castone2rifqa.Repository.RoomListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomListingService {

    private final RoomListingRepository roomListingRepository;
    private final RenterRepository renterRepository;

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
        List<RoomListing> listings = roomListingRepository.findRoomListingsByRentAmountLessThanEqualAndAvailable(maxRent, true);
        if (listings.isEmpty()) {
            throw new ApiException("No available listings found with rent up to " + maxRent);
        }
        return listings;
    }

    public Boolean addRoomListing(RoomListing roomListing) {
        Renter renter = renterRepository.findRenterById(roomListing.getRenterId());
        if (renter == null) {
            throw new ApiException("Cannot create listing: Renter not found with ID: " + roomListing.getRenterId());
        }

        roomListing.setAvailable(true);
        roomListingRepository.save(roomListing);
        return true;
    }

    public Boolean updateRoomListing(Integer id, RoomListing updatedListing) {
        RoomListing listing = roomListingRepository.findRoomListingById(id);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + id);
        }

        Renter renter = renterRepository.findRenterById(updatedListing.getRenterId());
        if (renter == null) {
            throw new ApiException("Cannot update listing: Renter not found with ID: " + updatedListing.getRenterId());
        }

        listing.setRenterId(updatedListing.getRenterId());
        listing.setTitle(updatedListing.getTitle());
        listing.setCity(updatedListing.getCity());
        listing.setDistrict(updatedListing.getDistrict());
        listing.setRentAmount(updatedListing.getRentAmount());
        listing.setDescription(updatedListing.getDescription());
        listing.setAvailable(updatedListing.getAvailable());

        roomListingRepository.save(listing);
        return true;
    }

    public Boolean toggleListingAvailability(Integer id) {
        RoomListing listing = roomListingRepository.findRoomListingById(id);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + id);
        }

        listing.setAvailable(!listing.getAvailable());
        roomListingRepository.save(listing);
        return true;
    }

    public Boolean deleteRoomListing(Integer id) {
        RoomListing listing = roomListingRepository.findRoomListingById(id);
        if (listing == null) {
            throw new ApiException("Room listing not found with ID: " + id);
        }

        roomListingRepository.delete(listing);
        return true;
    }
}