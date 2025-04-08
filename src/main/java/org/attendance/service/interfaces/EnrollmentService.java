package org.attendance.service.interfaces;

import org.attendance.dto.response.CourseResponseDTO;
import org.attendance.dto.response.StudentResponseDTO;

import java.util.List;

public interface EnrollmentService {
    void enrollStudentToCourse(long studentId, long courseId);
    List<StudentResponseDTO> getStudentsByCourse(long courseId);
    List<CourseResponseDTO> getCoursesByStudent(long studentId);
}
