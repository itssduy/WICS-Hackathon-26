package com.example.demo.controller;
import com.example.demo.model.AuthorizationRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import com.example.demo.model.SignUpRequest;
import org.springframework.web.bind.annotation.*;
import com.example.demo.model.AuthResponse;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/auth")
public class AuthorizationController {

    @PostMapping("/signup")
    public AuthResponse signup(@RequestBody SignUpRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return new AuthResponse("Error: Passwords do not match");
        }

        return new AuthResponse("User created: " + request.getEmail());
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthorizationRequest request) {
        return new AuthResponse("Logged in: " + request.getEmail());
    }
}