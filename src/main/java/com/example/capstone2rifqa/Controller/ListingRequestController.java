package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.ListingRequest;
import com.example.capstone2rifqa.Service.ListingRequestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/listing-request")
@RequiredArgsConstructor
public class ListingRequestController {

    private final ListingRequestService listingRequestService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllListingRequests() {
        return ResponseEntity.status(200).body(listingRequestService.getAllListingRequests());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getListingRequestById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(listingRequestService.getListingRequestById(id));
    }

    @GetMapping("/get-by-listing/{listingId}")
    public ResponseEntity<?> getRequestsByListingId(@PathVariable Integer listingId) {
        return ResponseEntity.status(200).body(listingRequestService.getRequestsByListingId(listingId));
    }

    @GetMapping("/get-pending-by-listing/{listingId}")
    public ResponseEntity<?> getPendingRequestsByListingId(@PathVariable Integer listingId) {
        return ResponseEntity.status(200).body(listingRequestService.getPendingRequestsByListingId(listingId));
    }

    @GetMapping("/get-by-requester/{requesterId}")
    public ResponseEntity<?> getRequestsByRequesterId(@PathVariable Integer requesterId) {
        return ResponseEntity.status(200).body(listingRequestService.getRequestsByRequesterId(requesterId));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addListingRequest(@RequestBody @Valid ListingRequest listingRequest) {
        listingRequestService.addListingRequest(listingRequest);
        return ResponseEntity.status(200).body(new ApiResponse("Listing request sent successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateListingRequest(@PathVariable Integer id, @RequestBody @Valid ListingRequest listingRequest) {
        listingRequestService.updateListingRequest(id, listingRequest);
        return ResponseEntity.status(200).body(new ApiResponse("Listing request updated successfully"));
    }

    @PutMapping("/accept/requestid/{requestId}/renterid/{renterId}")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer requestId, @PathVariable Integer renterId) {
        listingRequestService.acceptRequest(requestId, renterId);
        return ResponseEntity.status(200).body(new ApiResponse("Listing request accepted successfully"));
    }

    @PutMapping("/reject/requestid/{requestId}/renterid/{renterId}")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer requestId, @PathVariable Integer renterId) {
        listingRequestService.rejectRequest(requestId, renterId);
        return ResponseEntity.status(200).body(new ApiResponse("Listing request rejected successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteListingRequest(@PathVariable Integer id) {
        listingRequestService.deleteListingRequest(id);
        return ResponseEntity.status(200).body(new ApiResponse("Listing request deleted successfully"));
    }
}