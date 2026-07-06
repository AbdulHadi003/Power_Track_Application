package com.powertrack.dto.mapper;


import com.powertrack.dto.request.notification.NotificationRequest;
import com.powertrack.dto.response.notification.NotificationResponseDTO;
import com.powertrack.entity.SystemNotification;
import com.powertrack.entity.User;
import com.powertrack.enums.NotificationType;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public static SystemNotification toEntity(NotificationRequest dto, User user, User createdBy) {
        SystemNotification notification = new SystemNotification();
        notification.setUser(user); // null for broadcast
        notification.setTitle(dto.getTitle());
        notification.setMessage(dto.getMessage());
        notification.setNotificationType(NotificationType.valueOf(dto.getNotificationType()));
        notification.setIsRead(false);
        notification.setCreatedBy(createdBy);
        notification.setExpiresAt(dto.getExpiresAt());
        return notification;
    }

    public static NotificationResponseDTO toDTO(SystemNotification notification) {
        if (notification == null) {
            return null;
        }

        return NotificationResponseDTO.builder()
                .id(notification.getId())
                .userId(notification.getUserId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .notificationType(notification.getNotificationType().name())
                .isRead(notification.getIsRead())
                .isBroadcast(notification.isBroadcast())
                .createdById(notification.getCreatedById())
                .createdByName(notification.getCreatedBy() != null ? notification.getCreatedBy().getName() : null)
                .createdAt(notification.getCreatedAt())
                .expiresAt(notification.getExpiresAt())
                .isExpired(notification.isExpired())
                .build();
    }
}