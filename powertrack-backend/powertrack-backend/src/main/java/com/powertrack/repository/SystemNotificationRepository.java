package com.powertrack.repository;

import com.powertrack.entity.SystemNotification;
import com.powertrack.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SystemNotificationRepository extends JpaRepository<SystemNotification, Long> {

    List<SystemNotification> findByUserId(Long userId);

    List<SystemNotification> findByUserIdAndIsRead(Long userId, Boolean isRead);

    @Query("SELECT n FROM SystemNotification n WHERE n.user IS NULL ORDER BY n.createdAt DESC")
    List<SystemNotification> findBroadcastNotifications();

    // Enum-based
    List<SystemNotification> findByNotificationType(NotificationType notificationType);

    @Query("SELECT n FROM SystemNotification n WHERE n.user.id = ?1 " +
            "AND n.createdAt >= ?2 ORDER BY n.createdAt DESC")
    List<SystemNotification> findRecentUserNotifications(Long userId, LocalDateTime cutoffDate);

    @Query("SELECT n FROM SystemNotification n WHERE " +
            "(n.user.id = ?1 OR n.user IS NULL) " +
            "AND (n.expiresAt IS NULL OR n.expiresAt > CURRENT_TIMESTAMP) " +
            "ORDER BY n.createdAt DESC")
    List<SystemNotification> findActiveNotificationsForUser(Long userId);

    @Query("SELECT COUNT(n) FROM SystemNotification n " +
            "WHERE n.user.id = ?1 AND n.isRead = false")
    long countUnreadNotifications(Long userId);

    @Query("SELECT n FROM SystemNotification n WHERE n.expiresAt < CURRENT_TIMESTAMP")
    List<SystemNotification> findExpiredNotifications();

    List<SystemNotification> findByCreatedById(Long adminId);

    @Query("SELECT n FROM SystemNotification n WHERE n.createdAt < ?1")
    List<SystemNotification> findOldNotifications(LocalDateTime cutoffDate);
}
