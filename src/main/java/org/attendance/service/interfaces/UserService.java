package org.attendance.service.interfaces;

import org.attendance.dto.UserRequestDTO;
import org.attendance.entity.User;

public interface UserService {
    void registerUser(UserRequestDTO userDTO);
}
