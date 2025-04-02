package org.attendance.controller;

import org.attendance.entity.Course;
import org.attendance.service.interfaces.CourseService;
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
    public ResponseEntity<String> createCourse(@RequestBody Course course) {
        try {
            courseService.saveCourse(course);
            return ResponseEntity.ok("Course created successfully \n" + "courseName : " + course.getCourseName()
                    + "\ncrn : " + course.getCrn() + "\ndepartment : " + course.getDepartment() + "\nsemester : " + course.getSemester());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating course: " + e.getMessage());
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
