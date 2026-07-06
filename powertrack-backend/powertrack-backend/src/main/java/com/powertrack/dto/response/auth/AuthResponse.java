package com.powertrack.dto.response.auth;

import com.powertrack.dto.response.user.UserResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private UserResponseDTO user;
    private String message;
    private String sessionId;  // ⭐ Add this
}
