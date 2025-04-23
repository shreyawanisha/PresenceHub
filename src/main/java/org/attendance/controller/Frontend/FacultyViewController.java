package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @GetMapping("/qr")
    public String showQRPage(@RequestParam Long courseId, Model model) {
        model.addAttribute("courseId", courseId);
        return "faculty_qr";
    }
}
