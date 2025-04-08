package org.attendance.service;

import org.attendance.dao.FacultyDAO;
import org.attendance.dao.UserDAO;
import org.attendance.dto.request.FacultyRequestDTO;
import org.attendance.dto.response.FacultyResponseDTO;
import org.attendance.entity.Faculty;
import org.attendance.entity.User;
import org.attendance.enums.RoleType;
import org.attendance.service.interfaces.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class FacultyServiceImpl implements FacultyService {

    private final FacultyDAO facultyDAO;
    private final UserDAO userDAO;

    public FacultyServiceImpl(FacultyDAO facultyDAO, UserDAO userDAO) {
        this.facultyDAO = facultyDAO;
        this.userDAO = userDAO;
    }

    @Override
    public User createFaculty(FacultyRequestDTO dto) {
        User user = (userDAO.findById(dto.getUserId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole().getName() != RoleType.FACULTY) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not have FACULTY role");
        }

        if (facultyDAO.findByUserId(dto.getUserId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faculty already exists");
        }

        Faculty faculty = new Faculty();
        faculty.setUser(user);
        faculty.setDepartment(dto.getDepartment());

        facultyDAO.save(faculty);
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacultyResponseDTO> getAllFaculty() {
        return facultyDAO.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public FacultyResponseDTO getFacultyByUserId(Long userId) {
        Faculty faculty = facultyDAO.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found for user ID: " + userId));
        return mapToDto(faculty);
    }

    private FacultyResponseDTO mapToDto(Faculty faculty) {
        FacultyResponseDTO dto = new FacultyResponseDTO();
        dto.setId(faculty.getId());
        dto.setDepartment(faculty.getDepartment());
        dto.setEmail(faculty.getUser().getEmail());
        dto.setUsername(faculty.getUser().getUsername());
        return dto;
    }
}