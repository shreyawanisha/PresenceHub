package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.LoginRequestDTO;
import org.attendance.dto.AuthResponseDTO;
import org.attendance.dto.RegistrationRequestDTO;
import org.attendance.service.interfaces.UserService;
import org.attendance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        try {
            // Check if user already exists
            if (userService.existsByEmail(userDTO.getEmail())) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new AuthResponseDTO(null, "User with this email already exists."));
            }

            // Encrypt password
            String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
            userDTO.setPassword(encryptedPassword);
            userService.registerUser(userDTO);

            String token = jwtUtil.generateToken(userDTO.getEmail());
            return ResponseEntity.ok(new AuthResponseDTO(token, "Registration successful"));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new AuthResponseDTO(null, "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        try{
            userService.login(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
            String token = jwtUtil.generateToken(loginRequestDTO.getEmail());
            return ResponseEntity.ok(new AuthResponseDTO(token, "Login successful"));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: please check your email and password" + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("You have been logged out successfully.");
    }
}
