package org.attendance.service;

import org.attendance.dao.EnrollmentDAO;
import org.attendance.dao.FacultyDAO;
import org.attendance.dao.StudentDAO;
import org.attendance.dao.UserDAO;
import org.attendance.dto.request.StudentRequestDTO;
import org.attendance.dto.response.StudentResponseDTO;
import org.attendance.entity.*;
import org.attendance.enums.RoleType;
import org.attendance.service.interfaces.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO;
    private final UserDAO userDAO;
    private final FacultyDAO facultyDAO;
    private final EnrollmentDAO enrollmentDAO;

    public StudentServiceImpl(StudentDAO studentDAO, UserDAO userDAO, FacultyDAO facultyDAO, EnrollmentDAO enrollmentDAO) {
        this.studentDAO = studentDAO;
        this.userDAO = userDAO;
        this.facultyDAO = facultyDAO;
        this.enrollmentDAO = enrollmentDAO;
    }

    @Override
    public void registerStudent(StudentRequestDTO dto) {
        User user = userDAO.findById(dto.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        if (user.getRole().getName() != RoleType.STUDENT) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User does not have STUDENT role");
        }

        if (studentDAO.findByUserId(dto.getUserId()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student already exists for this user");
        }

        if (user.getRollNumber() == null || user.getDepartment() == null || user.getSemester() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is missing required student details");
        }

        Student student = new Student();
        student.setUser(user);
        student.setRollNumber(user.getRollNumber());
        student.setDepartment(user.getDepartment());
        student.setSemester(user.getSemester());

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

    @Override
    public List<StudentResponseDTO> getStudentsVisibleToCurrentUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getRole().getName() == RoleType.ADMIN) {
            return getAllStudents();
        }
        if (user.getRole().getName() == RoleType.FACULTY) {
            Faculty faculty = facultyDAO.findByUserId(user.getId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found for user ID: " + user.getId()));

            List<Long> courseIds = faculty.getCourses().stream()
                    .map(Course::getId)
                    .toList();
            List<Enrollment> enrollments = enrollmentDAO.findByCourseIdIs(courseIds);
            return enrollments.stream()
                    .map(Enrollment::getStudent).distinct()
                    .map(this::mapToDto)
                    .toList();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized access");
    }

    @Override
    public StudentResponseDTO getByEmail(String email) {
        Student student = studentDAO.findByUserEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found with email: " + email));
        return mapToDto(student);
    }

    @Override
    public Optional<StudentResponseDTO> findByUser(User user) {
      return studentDAO.findByUserId(user.getId())
                .map(this::mapToDto);
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
