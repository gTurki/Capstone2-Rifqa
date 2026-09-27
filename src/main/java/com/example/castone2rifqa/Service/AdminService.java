package com.example.castone2rifqa.Service;

import com.example.castone2rifqa.Api.ApiException;
import com.example.castone2rifqa.Entity.Admin;
import com.example.castone2rifqa.Entity.Renter;
import com.example.castone2rifqa.Entity.Report;
import com.example.castone2rifqa.Entity.ReportStatus;
import com.example.castone2rifqa.Entity.User;
import com.example.castone2rifqa.Repository.AdminRepository;
import com.example.castone2rifqa.Repository.RenterRepository;
import com.example.castone2rifqa.Repository.ReportRepository;
import com.example.castone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Boolean addAdmin(Admin admin) {
        Admin existingAdmin = adminRepository.findAdminByEmail(admin.getEmail());
        if (existingAdmin != null) {
            throw new ApiException("Email is already in use");
        }
        adminRepository.save(admin);
        return true;
    }

    public Boolean verifyUser(Integer adminId, Integer userId) {
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
        return true;
    }

    public Boolean verifyRenter(Integer adminId, Integer renterId) {
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
        return true;
    }

    public Boolean updateReportStatus(Integer adminId, Integer reportId, ReportStatus status) {
        checkAdminExists(adminId);

        Report report = reportRepository.findReportById(reportId);
        if (report == null) {
            throw new ApiException("Report not found with ID: " + reportId);
        }
        if (status == ReportStatus.PENDING) {
            throw new ApiException("A report can only be marked as REVIEWED or DISMISSED");
        }
        if (report.getStatus() != ReportStatus.PENDING) {
            throw new ApiException("This report has already been " + report.getStatus());
        }

        report.setStatus(status);
        reportRepository.save(report);
        return true;
    }

    private void checkAdminExists(Integer adminId) {
        Admin admin = adminRepository.findAdminById(adminId);
        if (admin == null) {
            throw new ApiException("Admin not found with ID: " + adminId);
        }
    }
}