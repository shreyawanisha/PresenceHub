package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.*;
import org.attendance.entity.Course;
import org.attendance.service.interfaces.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addCourse(@RequestBody @Valid CourseRequestDTO courseDTO) {
        if (courseService.existsByCrn(courseDTO.getCrn())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(HttpStatus.CONFLICT.value(), "Course already exists with CRN: " + courseDTO.getCrn()));
        }

        Course course = new Course(courseDTO.getCrn(), courseDTO.getCourseName(), courseDTO.getDepartment(), courseDTO.getSemester());
        courseService.saveCourse(course);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse(HttpStatus.CREATED.value(), "Course added successfully."));
    }

    @GetMapping
    public ResponseEntity<?> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();

        final List<CourseResponseDTO> courseList = getCourseResponseDTOList(courses);

        return ResponseEntity.ok(courseList);
    }

    private static List<CourseResponseDTO> getCourseResponseDTOList(List<Course> courses) {
        return courses.stream()
                .map(course -> new CourseResponseDTO(
                        course.getId(),
                        course.getCrn(),
                        course.getCourseName(),
                        course.getDepartment(),
                        course.getSemester(),
                        course.getFaculties().stream()
                                .map(faculty -> new FacultyResponseDTO(
                                        faculty.getUser().getId(),
                                        faculty.getDepartment(),
                                        faculty.getUser().getEmail(),
                                        faculty.getUser().getUsername()))
                                .toList()
                ))
                .toList();
    }

    @PostMapping("/assign-faculties")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignFacultyToCourse(@RequestBody AssignCourseToFacultyRequestDTO requestDTO) {
        try {
            courseService.assignFacultyToCourse(requestDTO);
            return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), "Faculties assigned successfully to course ID: " + requestDTO.getCourseId()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(new ApiResponse(e.getStatusCode().value(), e.getReason()));
        }
    }
}