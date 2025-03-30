package org.attendance.controller;

import jakarta.validation.Valid;
import org.attendance.dto.UserRequestDTO;
import org.attendance.service.interfaces.UserService;
import org.attendance.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, JwtUtil jwtUtil, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRequestDTO userDTO) {
        // Encrypt the password before saving
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());
        userDTO.setPassword(encryptedPassword);

        userService.registerUser(userDTO);

        String token = jwtUtil.generateToken(userDTO.getEmail());

        return ResponseEntity.ok().body(" {\"token\": \"" + token + "\"}");
    }

}
