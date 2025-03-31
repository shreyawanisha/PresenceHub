package org.attendance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {

    @Email
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Password is required")
    private String password;

    public LoginRequestDTO() {
    }
    public LoginRequestDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
