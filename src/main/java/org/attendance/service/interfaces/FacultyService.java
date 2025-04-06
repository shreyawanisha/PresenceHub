package org.attendance.service.interfaces;

import org.attendance.dto.FacultyRequestDTO;
import org.attendance.dto.FacultyResponseDTO;
import org.attendance.entity.User;

import java.util.List;

public interface FacultyService {
    User createFaculty(FacultyRequestDTO facultyDTO);
    List<FacultyResponseDTO> getAllFaculty();
    FacultyResponseDTO getFacultyByUserId(Long id);
}