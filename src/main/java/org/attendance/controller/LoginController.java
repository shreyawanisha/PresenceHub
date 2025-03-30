package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.LoginRequestDTO;
import org.attendance.dto.LoginResponseDTO;
import org.attendance.service.interfaces.UserService;
import org.attendance.util.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/")
public class LoginController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
       try{
           userService.login(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
           String token = jwtUtil.generateToken(loginRequestDTO.getEmail());
           return ResponseEntity.ok(new LoginResponseDTO(token, "Login successful"));
       }catch (Exception e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed: " + e.getMessage());
       }
    }
}
