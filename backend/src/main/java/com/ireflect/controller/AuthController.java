package com.ireflect.controller;

import com.ireflect.document.UserDocument;
import com.ireflect.dto.ApiError;
import com.ireflect.dto.AuthResponse;
import com.ireflect.dto.LoginRequest;
import com.ireflect.dto.SignupRequest;
import com.ireflect.repository.UserRepository;
import com.ireflect.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(
                new ApiError(400, "VALIDATION_ERROR", "Passwords do not match", "/api/auth/signup"));
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiError(409, "VALIDATION_ERROR", "Username already taken", "/api/auth/signup"));
        }

        UserDocument user = new UserDocument(
            request.getUsername(),
            passwordEncoder.encode(request.getPassword())
        );
        user = userRepository.save(user);

        String token = jwtService.generateToken(
            user.getId(), user.getUsername(),
            user.getPlan().name(), user.getRole().name()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(
            new AuthResponse(user.getId(), user.getUsername(),
                user.getPlan().name(), user.getSubscriptionStatus().name(), token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // Try username first, then email
        var optUser = userRepository.findByUsername(request.getUsernameOrEmail());
        if (optUser.isEmpty()) {
            optUser = userRepository.findByEmail(request.getUsernameOrEmail());
        }

        if (optUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiError(401, "UNAUTHORIZED", "Invalid credentials", "/api/auth/login"));
        }

        UserDocument user = optUser.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiError(401, "UNAUTHORIZED", "Invalid credentials", "/api/auth/login"));
        }

        String token = jwtService.generateToken(
            user.getId(), user.getUsername(),
            user.getPlan().name(), user.getRole().name()
        );

        return ResponseEntity.ok(
            new AuthResponse(user.getId(), user.getUsername(),
                user.getPlan().name(), user.getSubscriptionStatus().name(), token));
    }
}
