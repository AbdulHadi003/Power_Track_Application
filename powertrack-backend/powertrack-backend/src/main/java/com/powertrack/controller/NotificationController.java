package com.powertrack.controller;

import com.powertrack.dto.request.notification.NotificationRequest;
import com.powertrack.dto.response.notification.NotificationResponseDTO;
import com.powertrack.service.NotificationService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final SecurityUtils securityUtils;

    @GetMapping("/my-notifications")
    public ResponseEntity<List<NotificationResponseDTO>> getMyNotifications() {
        Long userId = securityUtils.getCurrentUserId();
        List<NotificationResponseDTO> notifications = notificationService.getMyNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/my-notifications/unread")
    public ResponseEntity<List<NotificationResponseDTO>> getMyUnreadNotifications() {
        Long userId = securityUtils.getCurrentUserId();
        List<NotificationResponseDTO> notifications = notificationService.getMyUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/my-notifications/active")
    public ResponseEntity<List<NotificationResponseDTO>> getActiveNotificationsForUser() {
        Long userId = securityUtils.getCurrentUserId();
        List<NotificationResponseDTO> notifications = notificationService.getActiveNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/my-notifications/recent")
    public ResponseEntity<List<NotificationResponseDTO>> getRecentNotifications(
            @RequestParam(defaultValue = "7") int days) {
        Long userId = securityUtils.getCurrentUserId();
        List<NotificationResponseDTO> notifications = notificationService.getRecentNotifications(userId, days);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponseDTO> getNotificationById(@PathVariable Long id) {
        NotificationResponseDTO notification = notificationService.getNotificationById(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{id}/mark-read")
    public ResponseEntity<NotificationResponseDTO> markAsRead(@PathVariable Long id) {
        NotificationResponseDTO notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<String> markAllAsRead() {
        Long userId = securityUtils.getCurrentUserId();
        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok("All notifications marked as read");
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> countUnreadNotifications() {
        Long userId = securityUtils.getCurrentUserId();
        long count = notificationService.countUnreadNotifications(userId);
        return ResponseEntity.ok(count);
    }

    // Admin endpoints

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponseDTO>> getAllNotifications() {
        List<NotificationResponseDTO> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponseDTO>> getBroadcastNotifications() {
        List<NotificationResponseDTO> notifications = notificationService.getBroadcastNotifications();
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByType(@PathVariable String type) {
        List<NotificationResponseDTO> notifications = notificationService.getNotificationsByType(type);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponseDTO> createNotification(
            @Valid @RequestBody NotificationRequest request) {
        Long adminId = securityUtils.getCurrentUserId();
        NotificationResponseDTO notification = notificationService.createNotification(request, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @PostMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponseDTO> broadcastNotification(
            @Valid @RequestBody NotificationRequest request) {
        Long adminId = securityUtils.getCurrentUserId();
        NotificationResponseDTO notification = notificationService.broadcastNotification(request, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(notification);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok("Notification deleted successfully");
    }

    @DeleteMapping("/cleanup/expired")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteExpiredNotifications() {
        notificationService.deleteExpiredNotifications();
        return ResponseEntity.ok("Expired notifications deleted successfully");
    }

    @DeleteMapping("/cleanup/old")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteOldNotifications(@RequestParam int daysOld) {
        notificationService.deleteOldNotifications(daysOld);
        return ResponseEntity.ok("Old notifications deleted successfully");
    }
}