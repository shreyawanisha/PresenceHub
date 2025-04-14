package org.attendance.service.interfaces;
import org.attendance.dto.request.RegistrationRequestDTO;
import org.attendance.entity.User;

import java.util.List;

public interface UserService {
    void registerUser(RegistrationRequestDTO userDTO);
    void login(String email, String password);
    boolean existsByEmail(String email);
    User getByEmail(String email);
    List<User> getUnassignedStudentUsers();
    List<User> getUnassignedFacultyUsers();
}