package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.AdminDTO;
import com.example.capstone2rifqa.Entity.Admin;
import com.example.capstone2rifqa.Entity.Renter;
import com.example.capstone2rifqa.Entity.Report;
import com.example.capstone2rifqa.Entity.ReportStatus;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Repository.AdminRepository;
import com.example.capstone2rifqa.Repository.RenterRepository;
import com.example.capstone2rifqa.Repository.ReportRepository;
import com.example.capstone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;
    private final RenterRepository renterRepository;
    private final ReportRepository reportRepository;

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Integer id) {
        Admin admin = adminRepository.findAdminById(id);
        if (admin == null) {
            throw new ApiException("Admin not found with ID: " + id);
        }
        return admin;
    }

    public void addAdmin(Integer adminId, AdminDTO adminDTO) {
        checkAdminExists(adminId);

        Admin existingAdmin = adminRepository.findAdminByEmail(adminDTO.getEmail());
        if (existingAdmin != null) {
            throw new ApiException("Email is already in use");
        }

        Admin admin = new Admin();
        admin.setName(adminDTO.getName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(adminDTO.getPassword());

        adminRepository.save(admin);
    }

    public void verifyUser(Integer adminId, Integer userId) {
        checkAdminExists(adminId);

        User user = userRepository.findUserById(userId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + userId);
        }
        if (user.getIsVerified()) {
            throw new ApiException("User is already verified");
        }

        user.setIsVerified(true);
        userRepository.save(user);
    }

    public void verifyRenter(Integer adminId, Integer renterId) {
        checkAdminExists(adminId);

        Renter renter = renterRepository.findRenterById(renterId);
        if (renter == null) {
            throw new ApiException("Renter not found with ID: " + renterId);
        }
        if (renter.getIsVerified()) {
            throw new ApiException("Renter is already verified");
        }

        renter.setIsVerified(true);
        renterRepository.save(renter);
    }

    public void updateReportStatus(Integer adminId, Integer reportId, String status) {
        checkAdminExists(adminId);

        ReportStatus reportStatus;
        try {
            reportStatus = ReportStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid status: " + status + ". Allowed values: " + Arrays.toString(ReportStatus.values()));
        }

        Report report = reportRepository.findReportById(reportId);
        if (report == null) {
            throw new ApiException("Report not found with ID: " + reportId);
        }
        if (reportStatus == ReportStatus.PENDING) {
            throw new ApiException("A report can only be marked as REVIEWED or DISMISSED");
        }
        if (report.getStatus() != ReportStatus.PENDING) {
            throw new ApiException("This report has already been " + report.getStatus());
        }

        report.setStatus(reportStatus);
        reportRepository.save(report);
    }

    private void checkAdminExists(Integer adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("Admin not found with ID: " + adminId);
        }
    }
}