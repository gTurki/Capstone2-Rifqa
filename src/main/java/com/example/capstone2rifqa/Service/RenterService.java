package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.Entity.Renter;
import com.example.capstone2rifqa.Repository.RenterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RenterService {

    private final RenterRepository renterRepository;

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

    public Boolean updateRenter(Integer id, Renter updatedRenter) {
        Renter renter = renterRepository.findRenterById(id);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + id);
        }

        Renter emailCheck = renterRepository.findRenterByEmail(updatedRenter.getEmail());
        if (emailCheck != null && !emailCheck.getId().equals(id)) {
            throw new ApiException("Email is already taken by another renter");
        }

        renter.setName(updatedRenter.getName());
        renter.setEmail(updatedRenter.getEmail());
        renter.setPassword(updatedRenter.getPassword());
        renter.setPhoneNumber(updatedRenter.getPhoneNumber());
        renter.setCity(updatedRenter.getCity());

        renterRepository.save(renter);
        return true;
    }

    public Boolean deleteRenter(Integer id) {
        Renter renter = renterRepository.findRenterById(id);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + id);
        }
        renterRepository.delete(renter);
        return true;
    }
}