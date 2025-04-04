package org.attendance.controller;

import org.attendance.dao.UserDAO;
import org.attendance.dto.ApiResponse;
import org.attendance.dto.FacultyRequestDTO;
import org.attendance.entity.Faculty;
import org.attendance.entity.User;
import org.attendance.service.interfaces.FacultyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<?> createFaculty(@RequestBody FacultyRequestDTO facultyDTO) {
            facultyService.createFaculty(facultyDTO);
            final User user = userDAO.findById(facultyDTO.getUserId());
        final String message = String.format(
                "Faculty created successfully :\n UserId: %d\n User email: %s\n Department: %s",
                user.getId(), user.getEmail(), facultyDTO.getDepartment()
        );
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), message));
    }

    @GetMapping
    public ResponseEntity<?> getAllFaculties() {
       final List<Faculty> faculties = facultyService.getAllFaculty();
       return  ResponseEntity.ok(faculties);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<?> getFacultyByUserId(@PathVariable Long userId) {
        Faculty faculty = facultyService.getFacultyByUserId(userId);
        if (faculty == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Faculty not found for user ID: " + userId);
        }
        return ResponseEntity.ok(faculty);
    }
}
