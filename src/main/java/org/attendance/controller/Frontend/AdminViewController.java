package org.attendance.controller.Frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminViewController {

    @GetMapping("/admin/dashboard")
    public String getAdminView() {
        return "admin_dashboard";
    }
}
