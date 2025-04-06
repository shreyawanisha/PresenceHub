package org.attendance.service.interfaces;

import org.attendance.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    void saveCourse(Course course);
    List<Course> getAllCourses();
    boolean existsByCrn(String crn);
    Optional<Course> getById(Long id);
}