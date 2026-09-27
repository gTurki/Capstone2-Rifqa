package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.Renter;
import com.example.capstone2rifqa.Service.RenterService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/renter")
@RequiredArgsConstructor
public class RenterController {

    private final RenterService renterService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllRenters() {
        return ResponseEntity.status(200).body(renterService.getAllRenters());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRenterById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(renterService.getRenterById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRenter(@RequestBody @Valid Renter renter) {
        renterService.addRenter(renter);
        return ResponseEntity.status(200).body(new ApiResponse("Renter added successfully"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRenter(@PathVariable Integer id, @RequestBody @Valid Renter renter) {
        renterService.updateRenter(id, renter);
        return ResponseEntity.status(200).body(new ApiResponse("Renter updated successfully"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRenter(@PathVariable Integer id) {
        renterService.deleteRenter(id);
        return ResponseEntity.status(200).body(new ApiResponse("Renter deleted successfully"));
    }
}