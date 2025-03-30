package org.attendance.service;

import jakarta.transaction.Transactional;
import org.attendance.dao.RoleDAO;
import org.attendance.dao.UserDAO;
import org.attendance.dto.UserRequestDTO;
import org.attendance.entity.Role;
import org.attendance.entity.User;
import org.attendance.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDao;
    private final RoleDAO roleDao;

    @Autowired
    public UserServiceImpl(UserDAO userDao, RoleDAO roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    @Transactional
    public void registerUser(UserRequestDTO userDTO) {

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
}
