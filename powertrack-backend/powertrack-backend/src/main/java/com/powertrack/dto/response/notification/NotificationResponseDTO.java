package com.powertrack.dto.response.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long id;
    private Long userId; // null = broadcast
    private String title;
    private String message;
    private String notificationType;
    private Boolean isRead;
    private Boolean isBroadcast;
    private Long createdById;
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private Boolean isExpired;
}