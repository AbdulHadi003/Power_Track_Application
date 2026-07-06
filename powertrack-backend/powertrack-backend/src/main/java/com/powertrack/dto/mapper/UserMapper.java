package com.powertrack.dto.mapper;

import com.powertrack.dto.request.user.CreateUserRequest;
import com.powertrack.dto.request.user.UpdateUserRequest;
import com.powertrack.dto.response.user.UserResponseDTO;
import com.powertrack.entity.User;
import com.powertrack.enums.UserRole;
import com.powertrack.enums.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public static User toEntity(CreateUserRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(request.getAddress());
        user.setRole(UserRole.valueOf(request.getRole()));
        user.setStatus(UserStatus.ACTIVE);
        user.setFailedLoginAttempts(0);
        return user;
    }

    public static void updateEntityFromRequest(User user, UpdateUserRequest request) {
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAddress() != null && !request.getAddress().isBlank()) {
            user.setAddress(request.getAddress());
        }
    }
}