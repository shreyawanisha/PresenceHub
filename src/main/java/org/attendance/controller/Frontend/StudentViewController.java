package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentViewController {

    @GetMapping("/dashboard")
    public String studentDashboard() {
        return "student_dashboard";
    }

    @GetMapping("/attendance/view")
    public String studentAttendanceView() {
        return "student_attendance";
    }

    @GetMapping("/scan")
    public String showQRPage() {
        return "student_qr_scan";
    }
}