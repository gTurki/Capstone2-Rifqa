package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.RoomListing;
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

    @PostMapping("/add")
    public ResponseEntity<?> addRoomListing(@RequestBody @Valid RoomListing roomListing) {
        roomListingService.addRoomListing(roomListing);
        return ResponseEntity.status(200).body(new ApiResponse("Room listing added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoomListing(@PathVariable Integer id, @RequestBody @Valid RoomListing roomListing) {
        roomListingService.updateRoomListing(id, roomListing);
        return ResponseEntity.status(200).body(new ApiResponse("Room listing updated successfully"));
    }

    @PutMapping("/toggle-availability/{id}")
    public ResponseEntity<?> toggleListingAvailability(@PathVariable Integer id) {
        Boolean available = roomListingService.toggleListingAvailability(id);
        String status = available ? "available" : "not available";
        return ResponseEntity.status(200).body(new ApiResponse("Listing is now " + status));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoomListing(@PathVariable Integer id) {
        roomListingService.deleteRoomListing(id);
        return ResponseEntity.status(200).body(new ApiResponse("Room listing deleted successfully"));
    }
}