package org.attendance.service;

import org.attendance.dao.FacultyDAO;
import org.attendance.dao.StudentDAO;
import org.springframework.transaction.annotation.Transactional;
import org.attendance.dao.RoleDAO;
import org.attendance.dao.UserDAO;
import org.attendance.dto.request.RegistrationRequestDTO;
import org.attendance.entity.Role;
import org.attendance.entity.User;
import org.attendance.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final RoleDAO roleDAO;
    private final StudentDAO studentDAO;
    private final FacultyDAO facultyDAO;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleDAO roleDAO, StudentDAO studentDAO, FacultyDAO facultyDAO, BCryptPasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.studentDAO = studentDAO;
        this.facultyDAO = facultyDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void registerUser(RegistrationRequestDTO userDTO) {
        Role role = roleDAO.findByName(userDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Invalid role: " + userDTO.getRole()));

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(role);

        user.setRollNumber(userDTO.getRollNumber());
        user.setDepartment(userDTO.getDepartment());
        user.setSemester(userDTO.getSemester());

        userDAO.save(user);
    }

    @Override
    public void login(String email, String password) {
        User user = userDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found, please check your email or password"));

        validatePassword(user, password);
    }

    private void validatePassword(User user, String password) {
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDAO.findByEmail(email).isPresent();
    }

    @Override
    public User getByEmail(String email) {
        return userDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("This user doesn't exist, please register first"));
    }

    @Override
    public List<User> getUnassignedStudentUsers() {
        return userDAO.findAll().stream()
                .filter(user -> user.getRole().getName().name().equals("STUDENT"))
                .filter(user -> studentDAO.findByUserId(user.getId()).isEmpty())
                .toList();
    }

    @Override
    public List<User> getUnassignedFacultyUsers() {
        return userDAO.findAll().stream()
                .filter(user -> user.getRole().getName().name().equals("FACULTY"))
                .filter(user -> facultyDAO.findByUserId(user.getId()).isEmpty())
                .toList();
    }
}