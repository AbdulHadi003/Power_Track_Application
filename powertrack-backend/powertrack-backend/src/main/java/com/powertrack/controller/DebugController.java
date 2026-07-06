package com.powertrack.controller;

import com.powertrack.util.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/debug")
@RequiredArgsConstructor
public class DebugController {

    private final SecurityUtils securityUtils;

    @GetMapping("/auth-status")
    public ResponseEntity<Map<String, Object>> getAuthStatus(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        // Get authentication
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        log.info("=== AUTH STATUS DEBUG ===");
        log.info("Authentication object exists: {}", auth != null);

        if (auth != null) {
            log.info("Is Authenticated: {}", auth.isAuthenticated());
            log.info("Principal: {}", auth.getPrincipal());
            log.info("Authorities: {}", auth.getAuthorities());
        }

        response.put("isAuthenticated", auth != null && auth.isAuthenticated());

        if (auth != null && auth.isAuthenticated()) {
            response.put("principal", auth.getPrincipal());
            response.put("authorities", auth.getAuthorities().stream()
                    .map(Object::toString)
                    .toList());
        }

        // Get session info
        HttpSession session = request.getSession(false);
        log.info("Session exists: {}", session != null);

        if (session != null) {
            log.info("Session ID: {}", session.getId());
            response.put("sessionId", session.getId());
            response.put("sessionCreatedTime", session.getCreationTime());
            response.put("sessionLastAccessedTime", session.getLastAccessedTime());
            response.put("sessionMaxInactiveInterval", session.getMaxInactiveInterval());
        } else {
            response.put("session", "No active session found");
        }

        // Get current user info
        try {
            String email = SecurityUtils.getCurrentUserEmail();
            Long userId = securityUtils.getCurrentUserId();
            log.info("Current User Email: {}", email);
            log.info("Current User ID: {}", userId);
            response.put("currentUserEmail", email);
            response.put("currentUserId", userId);
        } catch (Exception ex) {
            log.warn("Error getting current user: {}", ex.getMessage());
            response.put("currentUser", "Error: " + ex.getMessage());
        }

        // Get user roles
        response.put("isAdmin", SecurityUtils.isCurrentUserAdmin());
        response.put("isCustomer", SecurityUtils.isCurrentUserCustomer());
        response.put("isSupport", SecurityUtils.isCurrentUserSupport());
        response.put("isFieldStaff", SecurityUtils.isCurrentUserFieldStaff());

        log.info("Auth Status Response: {}", response);
        log.info("=== END DEBUG ===");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/session-info")
    public ResponseEntity<Map<String, Object>> getSessionInfo(HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();

        HttpSession session = request.getSession(false);

        log.info("Session Info - Exists: {}", session != null);

        if (session != null) {
            response.put("exists", true);
            response.put("sessionId", session.getId());
            response.put("createdTime", session.getCreationTime());
            response.put("lastAccessedTime", session.getLastAccessedTime());
            response.put("maxInactiveInterval", session.getMaxInactiveInterval());
            response.put("isNew", session.isNew());
        } else {
            response.put("exists", false);
            response.put("message", "No active session");
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "OK"));
    }
}