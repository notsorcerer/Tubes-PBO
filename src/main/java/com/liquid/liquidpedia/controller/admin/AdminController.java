package com.liquid.liquidpedia.controller.admin;

import com.liquid.liquidpedia.dto.DashboardStats;
import com.liquid.liquidpedia.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("adminController")
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardStats stats = adminService.getDashboardStats();
        model.addAttribute("stats", stats);
        return "admin/dashboard";
    }
}
