package org.attendance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.attendance.enums.RoleType;

@Getter
@Setter
public class UserRequestDTO {
    @NotBlank(message = "Username is required")
    private String username;

    @Email
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    @NotNull(message = "Role is required")
    private RoleType role;

    public UserRequestDTO() {
    }

    public UserRequestDTO(String username, String email, String password, RoleType role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
