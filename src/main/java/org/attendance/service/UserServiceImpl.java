package org.attendance.service;

import jakarta.transaction.Transactional;
import org.attendance.dao.RoleDAO;
import org.attendance.dao.UserDAO;
import org.attendance.dto.RegistrationRequestDTO;
import org.attendance.entity.Role;
import org.attendance.entity.User;
import org.attendance.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDao;
    private final RoleDAO roleDao;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDao, RoleDAO roleDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void registerUser(RegistrationRequestDTO userDTO) {

        Role role = roleDao.findByName(userDTO.getRole());
        if(role == null) {
            throw new RuntimeException("Invalid role: " + userDTO.getRole());
        }
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(role);

        userDao.save(user);
    }

    @Override
    @Transactional
    public void login(String email, String password) {
        try{
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        validatePassword(user, password);
        } catch (Exception e) {
            throw new RuntimeException("Login failed: " + e.getMessage());
        }
    }

    private void validatePassword(User user, String password) {
        if (user.getPassword() == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.findByEmail(email) != null;
    }
}
