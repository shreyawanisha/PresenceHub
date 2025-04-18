package org.attendance.controller;

import org.attendance.dto.response.AttendanceRecordDTO;
import org.attendance.dto.request.AttendanceBatchRequestDTO;
import org.attendance.dto.response.ApiResponse;
import org.attendance.dto.response.AttendanceSummaryDTO;
import org.attendance.enums.AttendanceStatus;
import org.attendance.service.interfaces.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/mark")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> markAttendance(@RequestBody AttendanceBatchRequestDTO dto) {
        attendanceService.markAttendance(dto);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Attendance marked successfully."));
    }

    @PutMapping("/update-status/{attendanceId}")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> updateAttendanceStatus(@PathVariable Long attendanceId, @RequestParam AttendanceStatus status) {
        attendanceService.updateAttendanceStatus(attendanceId, status);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Attendance updated successfully."));
    }

    @GetMapping("/check-today")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<?> checkIfAttendanceMarkedToday(@RequestParam Long courseId) {
        boolean alreadyMarked = attendanceService.existsByCourseAndDate(courseId, LocalDate.now());
        return ResponseEntity.ok(Map.of("alreadyMarked", alreadyMarked));
    }

    @GetMapping("/by-course-and-date")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceByCourseAndDate(
            @RequestParam Long courseId,
            @RequestParam LocalDate date
    ) {
        List<AttendanceRecordDTO> records = attendanceService.getAttendanceRecords(courseId, date);
        return ResponseEntity.ok(records);
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/summary/my-courses")
    public ResponseEntity<List<AttendanceSummaryDTO>> getMyAttendanceSummary() {
        return ResponseEntity.ok(attendanceService.getAttendanceSummaryForCurrentStudent());
    }

    @GetMapping("/my-records")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<AttendanceRecordDTO>> getMyAttendanceByCourse(@RequestParam Long courseId) {
        List<AttendanceRecordDTO> records = attendanceService.getStudentRecordsByCourse(courseId);
        return ResponseEntity.ok(records);
    }

    @GetMapping("/report/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<AttendanceRecordDTO>> getAttendanceReportByCourse(
            @PathVariable Long courseId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search
    ) {
        try {
            LocalDate start = (startDate != null) ? LocalDate.parse(startDate) : null;
            LocalDate end = (endDate != null) ? LocalDate.parse(endDate) : null;

            List<AttendanceRecordDTO> report = attendanceService.getAttendanceReport(courseId, start, end, status, search);
            return ResponseEntity.ok(report);
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Use YYYY-MM-DD.");
        }
    }
}
