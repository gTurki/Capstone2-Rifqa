package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.Entity.Admin;
import com.example.capstone2rifqa.Service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllAdmins() {
        return ResponseEntity.status(200).body(adminService.getAllAdmins());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(adminService.getAdminById(id));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAdmin(@RequestBody @Valid Admin admin) {
        adminService.addAdmin(admin);
        return ResponseEntity.status(200).body(new ApiResponse("Admin added successfully"));
    }

    @PutMapping("/verify-user/adminid/{adminId}/userid/{userId}")
    public ResponseEntity<?> verifyUser(@PathVariable Integer adminId, @PathVariable Integer userId) {
        adminService.verifyUser(adminId, userId);
        return ResponseEntity.status(200).body(new ApiResponse("User verified successfully"));
    }

    @PutMapping("/verify-renter/adminid/{adminId}/renterid/{renterId}")
    public ResponseEntity<?> verifyRenter(@PathVariable Integer adminId, @PathVariable Integer renterId) {
        adminService.verifyRenter(adminId, renterId);
        return ResponseEntity.status(200).body(new ApiResponse("Renter verified successfully"));
    }

    @PutMapping("/update-report-status/adminid/{adminId}/reportid/{reportId}/status/{status}")
    public ResponseEntity<?> updateReportStatus(@PathVariable Integer adminId, @PathVariable Integer reportId, @PathVariable String status) {
        adminService.updateReportStatus(adminId, reportId, status);
        return ResponseEntity.status(200).body(new ApiResponse("Report status updated successfully"));
    }
}