package com.example.shoplaptop.controller;

import com.example.shoplaptop.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    private IUserService iUserService;

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }
}
