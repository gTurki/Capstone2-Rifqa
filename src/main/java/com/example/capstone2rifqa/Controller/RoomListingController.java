package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.DTO.RoomListingDTO;
import com.example.capstone2rifqa.Service.RoomListingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/room-listing")
@RequiredArgsConstructor
public class RoomListingController {

    private final RoomListingService roomListingService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRoomListings() {
        return ResponseEntity.status(200).body(roomListingService.getAllRoomListings());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRoomListingById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(roomListingService.getRoomListingById(id));
    }

    @GetMapping("/get-by-renter/{renterId}")
    public ResponseEntity<?> getRoomListingsByRenterId(@PathVariable Integer renterId) {
        return ResponseEntity.status(200).body(roomListingService.getRoomListingsByRenterId(renterId));
    }

    @GetMapping("/get-available-by-city/{city}")
    public ResponseEntity<?> getAvailableListingsByCity(@PathVariable String city) {
        return ResponseEntity.status(200).body(roomListingService.getAvailableListingsByCity(city));
    }

    @GetMapping("/get-available-by-max-rent/{maxRent}")
    public ResponseEntity<?> getAvailableListingsByMaxRent(@PathVariable Double maxRent) {
        return ResponseEntity.status(200).body(roomListingService.getAvailableListingsByMaxRent(maxRent));
    }

    @PostMapping("/add/renterid/{renterId}")
    public ResponseEntity<?> addRoomListing(@PathVariable Integer renterId, @RequestBody @Valid RoomListingDTO roomListingDTO) {
        roomListingService.addRoomListing(renterId, roomListingDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Room listing added successfully"));
    }

    @PutMapping("/update/listingid/{listingId}/renterid/{renterId}")
    public ResponseEntity<?> updateRoomListing(@PathVariable Integer listingId, @PathVariable Integer renterId, @RequestBody @Valid RoomListingDTO roomListingDTO) {
        roomListingService.updateRoomListing(listingId, renterId, roomListingDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Room listing updated successfully"));
    }

    @PutMapping("/toggle-availability/listingid/{listingId}/renterid/{renterId}")
    public ResponseEntity<?> toggleListingAvailability(@PathVariable Integer listingId, @PathVariable Integer renterId) {
        Boolean available = roomListingService.toggleListingAvailability(listingId, renterId);
        String status = available ? "available" : "not available";
        return ResponseEntity.status(200).body(new ApiResponse("Listing is now " + status));
    }

    @DeleteMapping("/delete/listingid/{listingId}/renterid/{renterId}")
    public ResponseEntity<?> deleteRoomListing(@PathVariable Integer listingId, @PathVariable Integer renterId) {
        roomListingService.deleteRoomListing(listingId, renterId);
        return ResponseEntity.status(200).body(new ApiResponse("Room listing deleted successfully"));
    }
}