package com.muyildirim.event_reservation.controller;

import com.muyildirim.event_reservation.dto.AuthResponse;
import com.muyildirim.event_reservation.dto.LoginRequest;
import com.muyildirim.event_reservation.dto.RegisterRequest;
import com.muyildirim.event_reservation.model.User;
import com.muyildirim.event_reservation.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*; 

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authManager;

    public AuthController(UserService userService, AuthenticationManager authManager) {
        this.userService = userService;
        this.authManager = authManager;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        User u = userService.register(req.getFullName(), req.getEmail(), req.getPassword());
        return ResponseEntity.ok(new AuthResponse("User registered: " + u.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );
        if (auth.isAuthenticated()) {
            return ResponseEntity.ok(new AuthResponse("Login successful"));
        }
        return ResponseEntity.status(401).body(new AuthResponse("Invalid credentials"));
    }
}
