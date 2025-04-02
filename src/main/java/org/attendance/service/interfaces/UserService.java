package org.attendance.service.interfaces;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.attendance.dto.RegistrationRequestDTO;

public interface UserService {
    void registerUser(RegistrationRequestDTO userDTO);
    void login(String email,  String password);

    boolean existsByEmail(String email);
}
