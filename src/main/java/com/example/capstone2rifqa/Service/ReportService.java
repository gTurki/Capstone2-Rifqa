package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.Entity.Report;
import com.example.capstone2rifqa.Entity.ReportStatus;
import com.example.capstone2rifqa.Entity.User;
import com.example.capstone2rifqa.Repository.ReportRepository;
import com.example.capstone2rifqa.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    public Report getReportById(Integer id) {
        Report report = reportRepository.findReportById(id);
        if (report == null) {
            throw new ApiException("Report not found with ID: " + id);
        }
        return report;
    }

    public List<Report> getReportsByStatus(String status) {
        ReportStatus reportStatus;
        try {
            reportStatus = ReportStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new ApiException("Invalid status: " + status + ". Allowed values: " + Arrays.toString(ReportStatus.values()));
        }

        List<Report> reports = reportRepository.findReportsByStatus(reportStatus);
        if (reports.isEmpty()) {
            throw new ApiException("No reports found with status: " + reportStatus);
        }
        return reports;
    }

    public List<Report> getReportsByReportedUserId(Integer reportedUserId) {
        User user = userRepository.findUserById(reportedUserId);
        if (user == null) {
            throw new ApiException("User not found with ID: " + reportedUserId);
        }
        List<Report> reports = reportRepository.findReportsByReportedUserId(reportedUserId);
        if (reports.isEmpty()) {
            throw new ApiException("No reports found against this user");
        }
        return reports;
    }

    public Boolean addReport(Report report) {
        if (report.getReporterId().equals(report.getReportedUserId())) {
            throw new ApiException("You cannot report yourself");
        }

        User reporter = userRepository.findUserById(report.getReporterId());
        if (reporter == null) {
            throw new ApiException("Reporter not found with ID: " + report.getReporterId());
        }
        User reportedUser = userRepository.findUserById(report.getReportedUserId());
        if (reportedUser == null) {
            throw new ApiException("Reported user not found with ID: " + report.getReportedUserId());
        }

        report.setStatus(ReportStatus.PENDING);
        reportRepository.save(report);
        return true;
    }

    // Status changes are handled by AdminService; users can only edit the reason while it's pending
    public Boolean updateReport(Integer id, Report updatedReport) {
        Report report = reportRepository.findReportById(id);
        if (report == null) {
            throw new ApiException("Report not found with ID: " + id);
        }
        if (report.getStatus() != ReportStatus.PENDING) {
            throw new ApiException("Only pending reports can be edited");
        }

        report.setReason(updatedReport.getReason());
        reportRepository.save(report);
        return true;
    }

    public Boolean deleteReport(Integer id) {
        Report report = reportRepository.findReportById(id);
        if (report == null) {
            throw new ApiException("Report not found with ID: " + id);
        }
        reportRepository.delete(report);
        return true;
    }
}