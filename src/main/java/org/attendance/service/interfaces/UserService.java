package org.attendance.service.interfaces;
import org.attendance.dto.RegistrationRequestDTO;

public interface UserService {
    void registerUser(RegistrationRequestDTO userDTO);
    void login(String email,  String password);

    boolean existsByEmail(String email);
}
