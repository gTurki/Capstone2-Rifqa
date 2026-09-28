package com.example.capstone2rifqa.Controller;

import com.example.capstone2rifqa.Api.ApiResponse;
import com.example.capstone2rifqa.DTO.ReportDTO;
import com.example.capstone2rifqa.Service.ReportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/get")
    public ResponseEntity<?> getAllReports() {
        return ResponseEntity.status(200).body(reportService.getAllReports());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getReportById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(reportService.getReportById(id));
    }

    @GetMapping("/get-by-status/{status}")
    public ResponseEntity<?> getReportsByStatus(@PathVariable String status) {
        return ResponseEntity.status(200).body(reportService.getReportsByStatus(status));
    }

    @GetMapping("/get-by-reported-user/{reportedUserId}")
    public ResponseEntity<?> getReportsByReportedUserId(@PathVariable Integer reportedUserId) {
        return ResponseEntity.status(200).body(reportService.getReportsByReportedUserId(reportedUserId));
    }

    @PostMapping("/add/reporterid/{reporterId}/reporteduserid/{reportedUserId}")
    public ResponseEntity<?> addReport(@PathVariable Integer reporterId, @PathVariable Integer reportedUserId, @RequestBody @Valid ReportDTO reportDTO) {
        reportService.addReport(reporterId, reportedUserId, reportDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Report submitted successfully"));
    }

    @PutMapping("/update/reportid/{id}/reporterid/{reporterId}")
    public ResponseEntity<?> updateReport(@PathVariable Integer id, @PathVariable Integer reporterId, @RequestBody @Valid ReportDTO reportDTO) {
        reportService.updateReport(id, reporterId, reportDTO);
        return ResponseEntity.status(200).body(new ApiResponse("Report updated successfully"));
    }

    @DeleteMapping("/delete/reportid/{id}/reporterid/{reporterId}")
    public ResponseEntity<?> deleteReport(@PathVariable Integer id, @PathVariable Integer reporterId) {
        reportService.deleteReport(id, reporterId);
        return ResponseEntity.status(200).body(new ApiResponse("Report deleted successfully"));
    }
}