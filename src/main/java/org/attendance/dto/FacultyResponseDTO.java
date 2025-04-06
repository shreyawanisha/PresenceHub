package org.attendance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FacultyResponseDTO {
    private Long id;
    private String department;
    private String email;
    private String username;
}