package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.request.EnrollmentCourseRequestDTO;
import org.attendance.dto.request.EnrollmentRequestDTO;
import org.attendance.dto.response.ApiResponse;
import org.attendance.dto.response.CourseResponseDTO;
import org.attendance.dto.response.StudentResponseDTO;
import org.attendance.entity.User;
import org.attendance.service.interfaces.EnrollmentService;
import org.attendance.service.interfaces.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/enrollments")
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;

    public EnrollmentController(EnrollmentService enrollmentService, StudentService studentService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
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

    @GetMapping("/my-courses")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<CourseResponseDTO>> getCoursesForCurrentStudent() {
        Long studentId = getCurrentStudentId();
        return ResponseEntity.ok(enrollmentService.getCoursesByStudent(studentId));
    }

    @PostMapping("/self-enroll")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<?> selfEnroll(@RequestBody EnrollmentCourseRequestDTO dto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        enrollmentService.selfEnrollCurrentStudent(user, dto.getCourseId());
        return ResponseEntity.ok(new ApiResponse(200, "Enrolled successfully"));
    }

    private Long getCurrentStudentId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User user) {
            return studentService.getByEmail(user.getEmail()).getId();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }
}
