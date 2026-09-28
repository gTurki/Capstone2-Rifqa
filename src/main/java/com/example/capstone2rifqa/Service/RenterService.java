package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.ChangePasswordDTO;
import com.example.capstone2rifqa.DTO.RenterUpdateDTO;
import com.example.capstone2rifqa.Entity.Renter;
import com.example.capstone2rifqa.Entity.RoomListing;
import com.example.capstone2rifqa.Repository.RenterRepository;
import com.example.capstone2rifqa.Repository.RoomListingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RenterService {

    private final RenterRepository renterRepository;
    private final RoomListingRepository roomListingRepository;

    public List<Renter> getAllRenters() {
        return renterRepository.findAll();
    }

    public Renter getRenterById(Integer id) {
        Renter renter = renterRepository.findRenterById(id);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + id);
        }
        return renter;
    }

    public Boolean addRenter(Renter renter) {
        Renter existingRenter = renterRepository.findRenterByEmail(renter.getEmail());
        if (existingRenter != null) {
            throw new ApiException("Email is already in use");
        }
        renter.setIsVerified(false);
        renterRepository.save(renter);
        return true;
    }

    // Profile fields only. Password has its own endpoint.
    public Boolean updateRenter(Integer id, RenterUpdateDTO renterUpdateDTO) {
        Renter renter = renterRepository.findRenterById(id);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + id);
        }

        Renter emailCheck = renterRepository.findRenterByEmail(renterUpdateDTO.getEmail());
        if (emailCheck != null && !emailCheck.getId().equals(id)) {
            throw new ApiException("Email is already taken by another renter");
        }

        renter.setName(renterUpdateDTO.getName());
        renter.setEmail(renterUpdateDTO.getEmail());
        renter.setPhoneNumber(renterUpdateDTO.getPhoneNumber());
        renter.setCity(renterUpdateDTO.getCity());

        renterRepository.save(renter);
        return true;
    }

    public Boolean changePassword(Integer id, ChangePasswordDTO changePasswordDTO) {
        Renter renter = renterRepository.findRenterById(id);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + id);
        }
        if (!renter.getPassword().equals(changePasswordDTO.getOldPassword())) {
            throw new ApiException("Old password is incorrect");
        }
        if (changePasswordDTO.getOldPassword().equals(changePasswordDTO.getNewPassword())) {
            throw new ApiException("New password must be different from the old password");
        }

        renter.setPassword(changePasswordDTO.getNewPassword());
        renterRepository.save(renter);
        return true;
    }

    public Boolean deleteRenter(Integer id) {
        Renter renter = renterRepository.findRenterById(id);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + id);
        }

        // Listings store the renter's ID, so deleting the renter first would leave listings with no owner
        List<RoomListing> listings = roomListingRepository.findRoomListingsByRenterId(id);
        if (!listings.isEmpty()) {
            throw new ApiException("This renter still has listings. Delete them first");
        }

        renterRepository.delete(renter);
        return true;
    }
}