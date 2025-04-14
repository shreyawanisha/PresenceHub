package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.response.UserResponseDTO;
import org.attendance.dto.request.LoginRequestDTO;
import org.attendance.dto.request.RegistrationRequestDTO;
import org.attendance.dto.response.ApiResponse;
import org.attendance.dto.response.AuthResponseDTO;
import org.attendance.entity.User;
import org.attendance.service.interfaces.UserService;
import org.attendance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid RegistrationRequestDTO userDTO) {
        if (userService.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(HttpStatus.CONFLICT.value(), "User with this email already exists."));
        }

        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encryptedPassword);

        userService.registerUser(userDTO);

        String token = jwtUtil.generateToken(userDTO.getEmail(), userDTO.getRole().name());

        return ResponseEntity.ok(new AuthResponseDTO(token, "Registration successful"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        try {
            userService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());

            String role = userService.getByEmail(loginRequestDTO.getEmail()).getRole().getName().name();
            String token = jwtUtil.generateToken(loginRequestDTO.getEmail(), role);

            return ResponseEntity.ok(new AuthResponseDTO(token, "Login successful"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(401, "Login failed: please check your email and password."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(new ApiResponse(HttpStatus.NO_CONTENT.value(), "You have been logged out successfully."));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unassigned-students")
    public ResponseEntity<List<UserResponseDTO>> getUnassignedStudentUsers() {
        List<User> users = userService.getUnassignedStudentUsers();
        List<UserResponseDTO> userResponseDTOS = users.stream().map(UserResponseDTO::new).toList();

        return ResponseEntity.ok(userResponseDTOS);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/unassigned-faculties")
    public ResponseEntity<List<UserResponseDTO>> getUnassignedFacultyUsers() {
        List<User> users = userService.getUnassignedFacultyUsers();
        List<UserResponseDTO> userResponseDTOS = users.stream().map(UserResponseDTO::new).toList();

        return ResponseEntity.ok(userResponseDTOS);
    }
}