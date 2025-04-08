package org.attendance.service.interfaces;
import org.attendance.dto.request.RegistrationRequestDTO;
import org.attendance.entity.User;

public interface UserService {
    void registerUser(RegistrationRequestDTO userDTO);
    void login(String email, String password);
    boolean existsByEmail(String email);
    User getByEmail(String email);
}