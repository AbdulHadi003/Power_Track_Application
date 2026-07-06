package com.powertrack.service;

import com.powertrack.dto.mapper.NotificationMapper;
import com.powertrack.dto.request.notification.NotificationRequest;
import com.powertrack.dto.response.notification.NotificationResponseDTO;
import com.powertrack.entity.SystemNotification;
import com.powertrack.entity.User;
import com.powertrack.enums.NotificationType;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.SystemNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final SystemNotificationRepository notificationRepository;
    private final UserService userService;

    @Transactional
    public NotificationResponseDTO createNotification(NotificationRequest request, Long adminId) {
        User admin = userService.getUserEntityById(adminId);

        // Create notification
        SystemNotification notification = new SystemNotification();
        notification.setTitle(request.getTitle());
        notification.setMessage(request.getMessage());
        notification.setNotificationType(NotificationType.valueOf(request.getNotificationType()));
        notification.setIsRead(false);
        notification.setCreatedBy(admin);
        notification.setExpiresAt(request.getExpiresAt());

        // Set user (null means broadcast to all users)
        if (request.getUserId() != null) {
            User user = userService.getUserEntityById(request.getUserId());
            notification.setUser(user);
        }

        SystemNotification savedNotification = notificationRepository.save(notification);
        return NotificationMapper.toDTO(savedNotification);
    }

    @Transactional
    public NotificationResponseDTO broadcastNotification(NotificationRequest request, Long adminId) {
        // Set userId to null for broadcast
        request.setUserId(null);
        return createNotification(request, adminId);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationResponseDTO getNotificationById(Long id) {
        SystemNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));
        return NotificationMapper.toDTO(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getMyNotifications(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getMyUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndIsRead(userId, false).stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getActiveNotificationsForUser(Long userId) {
        return notificationRepository.findActiveNotificationsForUser(userId).stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getBroadcastNotifications() {
        return notificationRepository.findBroadcastNotifications().stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getNotificationsByType(String type) {
        NotificationType notificationType = NotificationType.valueOf(type);
        return notificationRepository.findByNotificationType(notificationType).stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getRecentNotifications(Long userId, int days) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(days);
        return notificationRepository.findRecentUserNotifications(userId, cutoffDate).stream()
                .map(NotificationMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationResponseDTO markAsRead(Long notificationId) {
        SystemNotification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));

        notification.markAsRead();
        SystemNotification updatedNotification = notificationRepository.save(notification);
        return NotificationMapper.toDTO(updatedNotification);
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<SystemNotification> unreadNotifications =
                notificationRepository.findByUserIdAndIsRead(userId, false);

        unreadNotifications.forEach(SystemNotification::markAsRead);
        notificationRepository.saveAll(unreadNotifications);
    }

    @Transactional
    public void deleteNotification(Long id) {
        SystemNotification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", id));

        notificationRepository.delete(notification);
    }

    @Transactional
    public void deleteExpiredNotifications() {
        List<SystemNotification> expiredNotifications = notificationRepository.findExpiredNotifications();
        notificationRepository.deleteAll(expiredNotifications);
    }

    @Transactional
    public void deleteOldNotifications(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        List<SystemNotification> oldNotifications = notificationRepository.findOldNotifications(cutoffDate);
        notificationRepository.deleteAll(oldNotifications);
    }

    @Transactional(readOnly = true)
    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countUnreadNotifications(userId);
    }

    // Helper methods to send automatic notifications

    @Transactional
    public void notifyBillGenerated(Long userId, String billMonth, Double amount) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("New Bill Generated");
        request.setMessage(String.format("Your bill for %s has been generated. Amount: Rs. %.2f",
                billMonth, amount));
        request.setNotificationType("INFO");

        User admin = userService.getUserEntityByEmail("admin@powertrack.com"); // System admin
        createNotification(request, admin.getId());
    }

    @Transactional
    public void notifyPaymentReceived(Long userId, String receiptNumber, Double amount) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("Payment Received");
        request.setMessage(String.format("Payment of Rs. %.2f received successfully. Receipt: %s",
                amount, receiptNumber));
        request.setNotificationType("INFO");

        User admin = userService.getUserEntityByEmail("admin@powertrack.com");
        createNotification(request, admin.getId());
    }

    @Transactional
    public void notifyMeterRequestStatus(Long userId, String status, String meterNumber) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("Meter Request " + status);
        request.setMessage(String.format("Your meter request has been %s. Meter Number: %s",
                status.toLowerCase(), meterNumber != null ? meterNumber : "N/A"));
        request.setNotificationType(status.equals("APPROVED") ? "INFO" : "WARNING");

        User admin = userService.getUserEntityByEmail("admin@powertrack.com");
        createNotification(request, admin.getId());
    }

    @Transactional
    public void notifyComplaintStatus(Long userId, String complaintToken, String status) {
        NotificationRequest request = new NotificationRequest();
        request.setUserId(userId);
        request.setTitle("Complaint Status Update");
        request.setMessage(String.format("Your complaint (%s) status: %s", complaintToken, status));
        request.setNotificationType("INFO");

        User admin = userService.getUserEntityByEmail("admin@powertrack.com");
        createNotification(request, admin.getId());
    }
}