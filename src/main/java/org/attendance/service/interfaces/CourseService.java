package org.attendance.service.interfaces;

import org.attendance.entity.Course;

import java.util.List;

public interface CourseService {
    void saveCourse(Course course);
    Course findCourseById(Long id);
    List<Course> getAllCourses();
    boolean existsByCrn(String crn);
}
