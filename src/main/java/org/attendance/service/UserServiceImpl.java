package org.attendance.service;

import org.springframework.transaction.annotation.Transactional;
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
@Transactional
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
    public void registerUser(RegistrationRequestDTO userDTO) {
        Role role = roleDao.findByName(userDTO.getRole())
                .orElseThrow(() -> new RuntimeException("Invalid role: " + userDTO.getRole()));

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(role);

        userDao.save(user);
    }

    @Override
    public void login(String email, String password) {
        User user = userDao.findByEmail(email)
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
        return userDao.findByEmail(email).isPresent();
    }

    @Override
    public User getByEmail(String email) {
        return userDao.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("This user doesn't exist, please register first"));
    }
}