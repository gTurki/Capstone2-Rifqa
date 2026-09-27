package com.example.castone2rifqa.Repository;

import com.example.castone2rifqa.Entity.RoomListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomListingRepository extends JpaRepository<RoomListing, Integer> {

    RoomListing findRoomListingById(Integer id);

    List<RoomListing> findRoomListingsByRenterId(Integer renterId);

    List<RoomListing> findRoomListingsByCityAndAvailable(String city, Boolean available);

    List<RoomListing> findRoomListingsByRentAmountLessThanEqualAndAvailable(Double maxRent, Boolean available);
}