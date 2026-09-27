package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.RoomListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomListingRepository extends JpaRepository<RoomListing, Integer> {

    RoomListing findRoomListingById(Integer id);

    List<RoomListing> findRoomListingsByRenterId(Integer renterId);

    List<RoomListing> findRoomListingsByCityAndAvailable(String city, Boolean available);

    // Cheapest listings first
    @Query("select r from RoomListing r where r.rentAmount <= ?1 and r.available = true order by r.rentAmount asc")
    List<RoomListing> findAvailableListingsByMaxRent(Double maxRent);
}