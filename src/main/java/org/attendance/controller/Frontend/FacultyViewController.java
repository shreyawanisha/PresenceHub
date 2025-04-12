package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacultyViewController {

    @GetMapping("/faculty/dashboard")
    public String facultyDashboard() {
        return "faculty_dashboard";
    }
}
