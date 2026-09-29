package com.example.capstone2rifqa.Service;

import com.example.capstone2rifqa.Api.ApiException;
import com.example.capstone2rifqa.DTO.ReportDTO;
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

    public void addReport(Integer reporterId, Integer reportedUserId, ReportDTO reportDTO) {
        if (reporterId.equals(reportedUserId)) {
            throw new ApiException("You cannot report yourself");
        }

        User reporter = userRepository.findUserById(reporterId);
        if (reporter == null) {
            throw new ApiException("Reporter not found with ID: " + reporterId);
        }
        User reportedUser = userRepository.findUserById(reportedUserId);
        if (reportedUser == null) {
            throw new ApiException("Reported user not found with ID: " + reportedUserId);
        }

        Report existing = reportRepository.findReportByReporterIdAndReportedUserIdAndStatus(reporterId, reportedUserId, ReportStatus.PENDING);
        if (existing != null) {
            throw new ApiException("You already have a pending report against this user");
        }

        Report report = new Report();
        report.setReporterId(reporterId);
        report.setReportedUserId(reportedUserId);
        report.setReason(reportDTO.getReason());
        report.setStatus(ReportStatus.PENDING);

        reportRepository.save(report);
    }

    public void updateReport(Integer id, Integer reporterId, ReportDTO reportDTO) {
        Report report = getPendingReportForReporter(id, reporterId);

        report.setReason(reportDTO.getReason());
        reportRepository.save(report);
    }

    public void deleteReport(Integer id, Integer reporterId) {
        Report report = getPendingReportForReporter(id, reporterId);

        reportRepository.delete(report);
    }

    private Report getPendingReportForReporter(Integer reportId, Integer reporterId) {
        Report report = reportRepository.findReportById(reportId);
        if (report == null) {
            throw new ApiException("Report not found with ID: " + reportId);
        }
        if (!report.getReporterId().equals(reporterId)) {
            throw new ApiException("Only the user who submitted this report can make changes to it");
        }
        if (report.getStatus() != ReportStatus.PENDING) {
            throw new ApiException("This report has already been " + report.getStatus());
        }
        return report;
    }
}