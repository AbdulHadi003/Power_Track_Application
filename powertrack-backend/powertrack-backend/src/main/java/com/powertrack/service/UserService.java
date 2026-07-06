package com.powertrack.service;

import com.powertrack.dto.mapper.UserMapper;
import com.powertrack.dto.request.user.CreateUserRequest;
import com.powertrack.dto.request.user.UpdateUserRequest;
import com.powertrack.dto.response.user.UserResponseDTO;
import com.powertrack.entity.User;
import com.powertrack.enums.UserRole;
import com.powertrack.enums.UserStatus;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return UserMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        return UserMapper.toDTO(user);
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUsersByRole(String role) {
        UserRole userRole = UserRole.valueOf(role);
        return userRepository.findByRole(userRole).stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveCustomers() {
        return userRepository.findAllActiveCustomers().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveCSRs() {
        return userRepository.findAllActiveCSRs().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> getActiveFieldStaff() {
        return userRepository.findAllActiveFieldStaff().stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> searchUsers(String keyword) {
        return userRepository.searchUsers(keyword).stream()
                .map(UserMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO createUser(CreateUserRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create user
        User user = UserMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);

        User savedUser = userRepository.save(user);
        return UserMapper.toDTO(savedUser);
    }

    @Transactional
    public UserResponseDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        UserMapper.updateEntityFromRequest(user, request);
        User updatedUser = userRepository.save(user);

        return UserMapper.toDTO(updatedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // Soft delete - set status to DELETED
        user.setStatus(UserStatus.DELETED);
        userRepository.save(user);
    }

    @Transactional
    public void suspendUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        user.setStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }

    // Helper method to get User entity (for internal use by other services)
    @Transactional(readOnly = true)
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    public User getUserEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}