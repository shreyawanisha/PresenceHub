package org.attendance.controller;

import jakarta.validation.Valid;
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
        try {
            if (courseService.existsByCrn(courseDTO.getCrn())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body("Course with this CRN already exists.");
            }
            Course course = new Course(courseDTO.getCrn(), courseDTO.getCourseName(), courseDTO.getDepartment(), courseDTO.getSemester());
            courseService.saveCourse(course);
            return ResponseEntity.ok("Course added successfully.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add course: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Course>> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            return ResponseEntity.ok(courses);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching courses: " + e.getMessage());
        }
    }

}
