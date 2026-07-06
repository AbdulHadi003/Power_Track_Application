package com.powertrack.util;

import com.powertrack.entity.User;
import com.powertrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * Get current user's email from SecurityContext
     */
    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        return authentication.getName();
    }

    /**
     * Get current User entity
     */
    public User getCurrentUser() {
        String email = getCurrentUserEmail();
        if (email == null) {
            return null;
        }
        return userRepository.findByEmail(email).orElse(null);
    }

    /**
     * Get current user's ID
     */
    public Long getCurrentUserId() {
        User user = getCurrentUser();
        return user != null ? user.getId() : null;
    }

    /**
     * Check if current user is admin
     */
    public static boolean isCurrentUserAdmin() {
        return hasRole("ROLE_ADMIN");
    }

    /**
     * Check if current user is customer
     */
    public static boolean isCurrentUserCustomer() {
        return hasRole("ROLE_CUSTOMER");
    }

    /**
     * Check if current user is support/CSR
     */
    public static boolean isCurrentUserSupport() {
        return hasRole("ROLE_SUPPORT");
    }

    /**
     * Check if current user is field staff
     */
    public static boolean isCurrentUserFieldStaff() {
        return hasRole("ROLE_FIELD_STAFF");
    }

    /**
     * Generic role checker
     */
    private static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(role));
    }

    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}