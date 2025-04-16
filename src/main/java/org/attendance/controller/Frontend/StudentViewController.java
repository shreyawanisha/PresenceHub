package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class StudentViewController {

    @GetMapping("/student/dashboard")
    public String studentDashboard() {
        return "student_dashboard";
    }

    @GetMapping("/student/attendance/view")
    public String studentAttendanceView() {
        return "student_attendance";
    }
}