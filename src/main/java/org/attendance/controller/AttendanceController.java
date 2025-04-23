package org.attendance.controller;

import org.attendance.dao.ActiveQRTokenDAO;
import org.attendance.dto.request.QRMarkAttendanceRequestDTO;
import org.attendance.dto.response.AttendanceRecordDTO;
import org.attendance.dto.request.AttendanceBatchRequestDTO;
import org.attendance.dto.response.ApiResponse;
import org.attendance.dto.response.AttendanceSummaryDTO;
import org.attendance.entity.Course;
import org.attendance.enums.AttendanceStatus;
import org.attendance.service.interfaces.AttendanceService;
import org.attendance.service.interfaces.CourseService;
import org.attendance.service.interfaces.StudentService;
import org.attendance.util.QRAttendanceTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final StudentService studentService;
    private final QRAttendanceTokenUtil qRAttendanceTokenUtil;
    private final CourseService courseService;
    private final ActiveQRTokenDAO activeQRTokenDAO;

    public AttendanceController(AttendanceService attendanceService, StudentService studentService, QRAttendanceTokenUtil qRAttendanceTokenUtil, CourseService courseService, ActiveQRTokenDAO activeQRTokenDAO) {
        this.attendanceService = attendanceService;
        this.studentService = studentService;
        this.qRAttendanceTokenUtil = qRAttendanceTokenUtil;
        this.courseService = courseService;
        this.activeQRTokenDAO = activeQRTokenDAO;
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

    @PostMapping("/generate")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse> generateQRToken(@RequestParam Long courseId) {
        final Optional<Course> course = courseService.findById(courseId);
        if(course.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(HttpStatus.NOT_FOUND.value(), "Course not found"));
        }
        String token = attendanceService.generateQrToken(courseId);

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), token));
    }

    @PostMapping("/mark-qr")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> markAttendanceViaQR(@RequestBody QRMarkAttendanceRequestDTO dto) {
        Map<String, Object> claims = qRAttendanceTokenUtil.validateToken(dto.getQrToken());

        Long courseId = Long.parseLong(claims.get("courseId").toString());
        LocalDate date = LocalDate.parse(claims.get("date").toString());

        Long studentId = studentService.getCurrentStudentId();
        attendanceService.markSingleAttendance(courseId, studentId, date);

        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Attendance marked successfully"));
    }

    @PostMapping("/end-qr")
    @PreAuthorize("hasRole('FACULTY')")
    public ResponseEntity<ApiResponse> endQR(@RequestParam Long courseId) {
        attendanceService.endQRSession(courseId, LocalDate.now());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "QR session ended."));
    }

    @GetMapping("/has-active-qr")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Map<String, Boolean>> hasActiveQR(@RequestParam Long courseId) {
        boolean isActive =  attendanceService.hasActiveQR(courseId, LocalDate.now());
        return ResponseEntity.ok(Map.of("active", isActive));
    }
}
