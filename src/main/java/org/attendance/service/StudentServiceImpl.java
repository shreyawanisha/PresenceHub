package org.attendance.service;

import org.attendance.dao.StudentDAO;
import org.attendance.dao.UserDAO;
import org.attendance.dto.request.StudentRequestDTO;
import org.attendance.dto.response.StudentResponseDTO;
import org.attendance.entity.Student;
import org.attendance.entity.User;
import org.attendance.enums.RoleType;
import org.attendance.service.interfaces.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private StudentDAO studentDAO;
    private UserDAO userDAO;

    public StudentServiceImpl(StudentDAO studentDAO, UserDAO userDAO) {
        this.studentDAO = studentDAO;
        this.userDAO = userDAO;
    }

    @Override
    public void registerStudent(StudentRequestDTO dto) {
        User user = userDAO.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole().getName() != RoleType.STUDENT) {
            System.out.println(user.getRole().getName());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not have STUDENT role");
        }

        if (studentDAO.findByUserId(dto.getUserId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already exists for this user");
        }

        Student student = new Student();
        student.setUser(user);
        student.setRollNumber(dto.getRollNumber());
        student.setDepartment(dto.getDepartment());
        student.setSemester(dto.getSemester());

        studentDAO.save(student);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentDAO.findAll().stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    public StudentResponseDTO getByUserId(Long userId) {
        Student student = studentDAO.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found for user ID: " + userId));
        return mapToDto(student);
    }

    private StudentResponseDTO mapToDto(Student student) {
        StudentResponseDTO dto = new StudentResponseDTO();
        dto.setId(student.getId());
        dto.setEmail(student.getUser().getEmail());
        dto.setUsername(student.getUser().getUsername());
        dto.setRollNumber(student.getRollNumber());
        dto.setDepartment(student.getDepartment());
        dto.setSemester(student.getSemester());
        return dto;
    }
}
