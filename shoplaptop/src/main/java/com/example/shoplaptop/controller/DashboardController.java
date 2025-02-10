package com.example.shoplaptop.controller;

import com.example.shoplaptop.model.Users;
import com.example.shoplaptop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private IUserService iUserService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("countUsers", iUserService.countUsers());
        return "/dashboard/dashboard";
    }

    @GetMapping("/users")
    public String users() {
        return "redirect:/dashboard/users/list";
    }
}
