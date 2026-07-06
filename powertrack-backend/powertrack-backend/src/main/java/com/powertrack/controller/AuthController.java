package com.powertrack.controller;

import com.powertrack.dto.request.auth.ChangePasswordRequest;
import com.powertrack.dto.request.auth.LoginRequest;
import com.powertrack.dto.request.auth.RegisterRequest;
import com.powertrack.dto.response.auth.AuthResponse;
import com.powertrack.service.AuthService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SecurityUtils securityUtils;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register endpoint called for email: {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login endpoint called for email: {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        log.info("Logout endpoint called");
        // Spring Security handles the logout via logoutSuccessHandler
        // This endpoint just needs to exist and be called
        // The actual logout is handled by the LogoutSuccessHandler in SecurityConfig
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        log.info("Change password endpoint called for user: {}", securityUtils.getCurrentUserId());
        Long userId = securityUtils.getCurrentUserId();
        authService.changePassword(userId, request);

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Password changed successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<AuthResponse> getProfile() {
        log.info("Profile endpoint called for user: {}", securityUtils.getCurrentUserId());
        Long userId = securityUtils.getCurrentUserId();
        AuthResponse response = authService.getCurrentUserProfile(userId);
        return ResponseEntity.ok(response);
    }
}