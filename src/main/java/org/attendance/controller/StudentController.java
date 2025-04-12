package org.attendance.controller;

import org.attendance.dto.request.StudentRequestDTO;
import org.attendance.dto.response.ApiResponse;
import org.attendance.dto.response.StudentResponseDTO;
import org.attendance.entity.User;
import org.attendance.service.interfaces.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> registerStudent(@RequestBody StudentRequestDTO dto) {
            studentService.registerStudent(dto);
            String message = String.format(
                    "Student registered successfully:\nUserId: %d\nRollNumber: %s\nDepartment: %s\nSemester: %s",
                    dto.getUserId(), dto.getRollNumber(), dto.getDepartment(), dto.getSemester()
            );
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), message));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'FACULTY')")
    @GetMapping
    public ResponseEntity<?> getAllStudents() {
        try {
            List<StudentResponseDTO> allStudents = studentService.getStudentsVisibleToCurrentUser();
            return ResponseEntity.ok(allStudents);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching students", e);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getStudentByUserId(@PathVariable Long userId) {
        try {
            StudentResponseDTO dto = studentService.getByUserId(userId);
            return ResponseEntity.ok(dto);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching student", e);
        }
    }

    @PreAuthorize("hasAnyRole('STUDENT', 'ADMIN', 'FACULTY')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentStudent() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User user) {
            Optional<StudentResponseDTO> studentOpt = studentService.findByUser(user);

            if (studentOpt.isPresent()) {
                return ResponseEntity.ok(studentOpt.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse(404, "Student record not found for user"));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse(401, "Unauthorized or invalid principal"));
    }
}