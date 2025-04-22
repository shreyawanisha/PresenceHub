package org.attendance.service.interfaces;

import org.attendance.dto.response.AssignCourseToFacultyRequestDTO;
import org.attendance.dto.response.CourseResponseDTO;
import org.attendance.entity.Course;

import java.util.List;
import java.util.Optional;

public interface CourseService {
    void saveCourse(Course course);
    List<Course> getAllCourses();
    boolean existsByCrn(String crn);
    void assignFacultyToCourse(AssignCourseToFacultyRequestDTO requestDTO);
    List<CourseResponseDTO> getCoursesAssignedToCurrentFaculty();
}