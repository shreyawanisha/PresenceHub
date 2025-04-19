package org.attendance.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.attendance.dto.response.AttendanceRecordDTO;
import org.attendance.service.interfaces.AttendanceService;
import org.attendance.util.AttendancePdfExportUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/export")
public class AttendanceExportController {

    private final AttendanceService attendanceService;

    public AttendanceExportController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/pdf/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public void exportAttendancePdf(
            @PathVariable Long courseId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            HttpServletResponse response
    ) throws IOException {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceReport(courseId, startDate, endDate, status, search);

        if (records.isEmpty()) {
            response.sendError(HttpStatus.NOT_FOUND.value(), "No attendance records found to export.");
            return;
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_report.pdf");

        try {
            AttendancePdfExportUtil.exportToPdf(records, response.getOutputStream());
        } catch (Exception e) {
            response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to generate PDF: " + e.getMessage());
        }
    }
}