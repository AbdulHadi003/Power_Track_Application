package com.powertrack.dto.request.notification;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private Long userId; // null = broadcast to all users

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title cannot exceed 200 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(max = 2000, message = "Message cannot exceed 2000 characters")
    private String message;

    @Pattern(regexp = "INFO|WARNING|ALERT|ANNOUNCEMENT", message = "Invalid notification type")
    private String notificationType = "INFO";

    private LocalDateTime expiresAt; // Optional - when notification expires
}