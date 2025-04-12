package org.attendance.service.interfaces;

import org.attendance.dto.request.StudentRequestDTO;
import org.attendance.dto.response.StudentResponseDTO;
import org.attendance.entity.User;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    void registerStudent(StudentRequestDTO dto);
    List<StudentResponseDTO> getAllStudents();
    StudentResponseDTO getByUserId(Long userId);
    List<StudentResponseDTO> getStudentsVisibleToCurrentUser();
    StudentResponseDTO getByEmail(String email);
    Optional<StudentResponseDTO> findByUser(User user);
}
