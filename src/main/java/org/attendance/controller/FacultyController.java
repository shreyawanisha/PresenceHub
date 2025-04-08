package org.attendance.controller;

import org.attendance.dao.UserDAO;
import org.attendance.dto.response.ApiResponse;
import org.attendance.dto.request.FacultyRequestDTO;
import org.attendance.dto.response.FacultyResponseDTO;
import org.attendance.entity.User;
import org.attendance.service.interfaces.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/faculties")
public class FacultyController {

    private final FacultyService facultyService;
    private final UserDAO userDAO;

    public FacultyController(FacultyService facultyService, UserDAO userDAO) {
        this.facultyService = facultyService;
        this.userDAO = userDAO;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<?> createFaculty(@RequestBody FacultyRequestDTO facultyDTO) {
        final User user = facultyService.createFaculty(facultyDTO);
        final String message = String.format(
                "Faculty created successfully:\n UserId: %d\n User email: %s\n Department: %s",
                user.getId(), user.getEmail(), facultyDTO.getDepartment()
        );
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), message));
    }

    @GetMapping
    public ResponseEntity<?> getAllFaculties() {
        final List<FacultyResponseDTO> faculties = facultyService.getAllFaculty();
        return ResponseEntity.ok(faculties);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getFacultyByUserId(@PathVariable Long userId) {
        FacultyResponseDTO dto = facultyService.getFacultyByUserId(userId);
        if (dto == null) {
            boolean userExists = userDAO.findById(userId).isPresent();
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    userExists ? "Faculty not created yet for user ID: " + userId
                            : "User not found with ID: " + userId
            );
        }
        return ResponseEntity.ok(dto);
    }
}
