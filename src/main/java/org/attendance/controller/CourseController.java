package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.ApiResponse;
import org.attendance.dto.CourseRequestDTO;
import org.attendance.entity.Course;
import org.attendance.service.interfaces.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(courses);
    }
}