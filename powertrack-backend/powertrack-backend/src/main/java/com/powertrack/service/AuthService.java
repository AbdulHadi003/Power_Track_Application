package com.powertrack.service;

import com.powertrack.dto.mapper.UserMapper;
import com.powertrack.dto.request.auth.ChangePasswordRequest;
import com.powertrack.dto.request.auth.LoginRequest;
import com.powertrack.dto.request.auth.RegisterRequest;
import com.powertrack.dto.response.auth.AuthResponse;
import com.powertrack.dto.response.user.UserResponseDTO;
import com.powertrack.entity.User;
import com.powertrack.enums.UserRole;
import com.powertrack.enums.UserStatus;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final HttpServletRequest httpServletRequest;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user: {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);
        user.setFailedLoginAttempts(0);

        User savedUser = userRepository.save(user);
        log.info("User registered: {} with ID: {}", request.getEmail(), savedUser.getId());

        // Authenticate and persist to session
        persistAuthenticationToSession(request.getEmail(), request.getPassword());

        HttpSession session = httpServletRequest.getSession(true);
        log.info("Session created with ID: {}", session.getId());

        UserResponseDTO userDTO = UserMapper.toDTO(savedUser);
        return AuthResponse.builder()
                .user(userDTO)
                .message("Registration successful")
                .sessionId(session.getId())
                .build();
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for user: {}", request.getEmail());

        try {
            // Authenticate and persist to session
            persistAuthenticationToSession(request.getEmail(), request.getPassword());

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "email", request.getEmail()));

            if (user.getStatus() == UserStatus.DELETED) {
                throw new IllegalStateException("User account is deleted");
            }

            if (user.getStatus() == UserStatus.SUSPENDED) {
                throw new IllegalStateException("User account is suspended");
            }

            user.setLastLoginAt(LocalDateTime.now());
            user.setFailedLoginAttempts(0);
            userRepository.save(user);
            log.info("User record updated: {} - Last login: {}", request.getEmail(), user.getLastLoginAt());

            HttpSession session = httpServletRequest.getSession(true);
            log.info("Session created with ID: {}", session.getId());

            UserResponseDTO userDTO = UserMapper.toDTO(user);
            return AuthResponse.builder()
                    .user(userDTO)
                    .message("Login successful")
                    .sessionId(session.getId())
                    .build();

        } catch (Exception ex) {
            log.error("Login failed for user: {}", request.getEmail(), ex);

            userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
                int attempts = user.getFailedLoginAttempts() + 1;
                user.setFailedLoginAttempts(attempts);
                log.warn("Failed login attempt #{} for user: {}", attempts, request.getEmail());

                if (attempts >= 5) {
                    user.setStatus(UserStatus.SUSPENDED);
                    log.warn("User suspended after 5 failed attempts: {}", request.getEmail());
                }
                userRepository.save(user);
            });

            throw ex;
        }
    }

    @Transactional
    public void logout() {
        log.info("Logout request");
        SecurityContextHolder.clearContext();
        HttpSession session = httpServletRequest.getSession(false);
        if (session != null) {
            session.invalidate();
            log.info("Session invalidated");
        }
    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Password changed for user: {}", userId);
    }

    @Transactional(readOnly = true)
    public AuthResponse getCurrentUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        HttpSession session = httpServletRequest.getSession(false);
        UserResponseDTO userDTO = UserMapper.toDTO(user);
        return AuthResponse.builder()
                .user(userDTO)
                .message("Profile fetched successfully")
                .sessionId(session != null ? session.getId() : null)
                .build();
    }

    // ⭐⭐⭐ CRITICAL METHOD: Persist authentication to HTTP session
    private void persistAuthenticationToSession(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = httpServletRequest.getSession(true);
        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
        );

        log.info("User authenticated & SecurityContext saved to session: {}", session.getId());
    }

}