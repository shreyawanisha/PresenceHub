package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.request.EnrollmentRequestDTO;
import org.attendance.dto.response.ApiResponse;
import org.attendance.dto.response.CourseResponseDTO;
import org.attendance.dto.response.StudentResponseDTO;
import org.attendance.service.interfaces.EnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> enrollStudentToCourse(@RequestBody @Valid EnrollmentRequestDTO enrollmentRequestDTO) {
        final long studentId = enrollmentRequestDTO.getStudentId();
        final long courseId = enrollmentRequestDTO.getCourseId();
        enrollmentService.enrollStudentToCourse(studentId, courseId);
        String msg = String.format("Student (ID: %d) enrolled in Course (ID: %d)", studentId, courseId);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), msg));
    }

    @GetMapping("/students-by-course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByCourse(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getStudentsByCourse(courseId));
    }

    @GetMapping("/courses-by-student/{studentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(enrollmentService.getCoursesByStudent(studentId));
    }
}
