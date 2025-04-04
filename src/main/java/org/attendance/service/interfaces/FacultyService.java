package org.attendance.service.interfaces;

import org.attendance.dto.FacultyRequestDTO;
import org.attendance.entity.Faculty;

import java.util.List;

public interface FacultyService {
    void createFaculty(FacultyRequestDTO facultyDTO);
    List<Faculty> getAllFaculty();
    Faculty getFacultyByUserId(Long id);
}
