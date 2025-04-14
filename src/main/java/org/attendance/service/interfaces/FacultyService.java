package org.attendance.service.interfaces;

import org.attendance.dto.request.FacultyRequestDTO;
import org.attendance.dto.response.FacultyResponseDTO;
import org.attendance.entity.User;

import java.util.List;

public interface FacultyService {
    void registerFaculty(FacultyRequestDTO facultyDTO);
    List<FacultyResponseDTO> getAllFaculty();
    FacultyResponseDTO getFacultyByUserId(Long id);
}