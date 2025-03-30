package org.attendance.service.interfaces;

import org.attendance.dto.UserRequestDTO;

public interface UserService {
    void registerUser(UserRequestDTO userDTO);
    void login(String email,  String password);
}
