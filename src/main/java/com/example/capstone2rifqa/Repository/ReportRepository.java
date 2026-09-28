package com.example.capstone2rifqa.Repository;

import com.example.capstone2rifqa.Entity.Report;
import com.example.capstone2rifqa.Entity.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

    Report findReportById(Integer id);

    List<Report> findReportsByStatus(ReportStatus status);

    List<Report> findReportsByReportedUserId(Integer reportedUserId);

    Report findReportByReporterIdAndReportedUserIdAndStatus(Integer reporterId, Integer reportedUserId, ReportStatus status);
}