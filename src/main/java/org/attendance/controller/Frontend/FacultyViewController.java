package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/faculty")
public class FacultyViewController {

    @GetMapping("/dashboard")
    public String facultyDashboard() {
        return "faculty_dashboard";
    }


    @GetMapping("/attendance")
    public String attendancePage() {
        return "faculty_attendance";
    }

    @GetMapping("/attendance/update")
    public String updateAttendancePage() {
        return "update_attendance";
    }
}
