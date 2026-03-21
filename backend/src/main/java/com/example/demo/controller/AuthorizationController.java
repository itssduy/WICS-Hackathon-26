package com.example.demo.controller;

import com.example.demo.model.AuthorizationRequest;
import com.example.demo.model.SignUpRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    @PostMapping("/signup")
    public String signup(@RequestBody SignUpRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return "Passwords do not match";
        }

        return "User created: " + request.getEmail();
    }

    @PostMapping("/login")
    public String login(@RequestBody AuthorizationRequest request) {
        return "Logged in: " + request.getEmail();
    }
}