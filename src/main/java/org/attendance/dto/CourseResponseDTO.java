package org.attendance.dto;

import lombok.*;


@Data
@AllArgsConstructor
public class CourseResponseDTO {
    private Long id;
    private String crn;
    private String courseName;
    private String department;
    private Integer semester;
}