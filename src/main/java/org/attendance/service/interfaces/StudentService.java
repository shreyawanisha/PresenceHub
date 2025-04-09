package org.attendance.service.interfaces;

import org.attendance.dto.request.StudentRequestDTO;
import org.attendance.dto.response.StudentResponseDTO;

import java.util.List;

public interface StudentService {
    void registerStudent(StudentRequestDTO dto);
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO getByUserId(Long userId);

    List<StudentResponseDTO> getStudentsVisibleToCurrentUser();
}
