package com.example.shoplaptop.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionController {

    @PostMapping("/clear-session")
    public ResponseEntity<Void> clearSession(@RequestParam String key, HttpSession session) {
        session.removeAttribute(key);
        return ResponseEntity.ok().build();
    }
}
