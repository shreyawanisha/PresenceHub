package org.attendance.service;

import org.attendance.dao.FacultyDAO;
import org.attendance.dao.UserDAO;
import org.attendance.dto.FacultyRequestDTO;
import org.attendance.entity.Faculty;
import org.attendance.entity.User;
import org.attendance.service.interfaces.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyDAO facultyDAO;
    private final UserDAO userDAO;

    public FacultyServiceImpl(FacultyDAO facultyDAO, UserDAO userDAO) {
        this.facultyDAO = facultyDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void createFaculty(FacultyRequestDTO facultyRequestDTO) {
        User user = userDAO.findById(facultyRequestDTO.getUserId());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with ID: " + facultyRequestDTO.getUserId());
        }

        if (!user.getRole().getName().name().equals("FACULTY")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not have FACULTY role");
        }

        if (facultyDAO.findByUserId(facultyRequestDTO.getUserId()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Faculty already exists for this user");
        }

        Faculty faculty = new Faculty();
        faculty.setUser(user);
        faculty.setDepartment(facultyRequestDTO.getDepartment());

        facultyDAO.save(faculty);
    }

    @Override
    public List<Faculty> getAllFaculty() {
        return facultyDAO.findAll();
    }

    @Override
    public Faculty getFacultyByUserId(Long userId) {
        return facultyDAO.findByUserId(userId);
    }

}
