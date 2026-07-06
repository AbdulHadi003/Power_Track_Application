package com.powertrack.repository;

import com.powertrack.entity.ChatMessage;
import com.powertrack.enums.MessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByConversationId(Long conversationId);

    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = ?1 " +
            "AND m.deletedAt IS NULL ORDER BY m.timestamp ASC")
    List<ChatMessage> findConversationMessages(Long conversationId);

    @Query("SELECT m FROM ChatMessage m WHERE m.receiver.id = ?1 " +
            "AND m.isRead = false AND m.deletedAt IS NULL")
    List<ChatMessage> findUnreadMessages(Long userId);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.receiver.id = ?1 " +
            "AND m.isRead = false AND m.deletedAt IS NULL")
    long countUnreadMessages(Long userId);

    List<ChatMessage> findBySenderId(Long senderId);

    List<ChatMessage> findByMessageType(MessageType messageType);

    @Query("SELECT m FROM ChatMessage m WHERE m.conversation.id = ?1 " +
            "AND m.deletedAt IS NULL ORDER BY m.timestamp DESC")
    List<ChatMessage> findRecentMessages(Long conversationId);

    @Query("SELECT m FROM ChatMessage m WHERE " +
            "((m.sender.id = ?1 AND m.receiver.id = ?2) OR (m.sender.id = ?2 AND m.receiver.id = ?1)) " +
            "AND m.deletedAt IS NULL ORDER BY m.timestamp ASC")
    List<ChatMessage> findMessagesBetweenUsers(Long userId1, Long userId2);

    List<ChatMessage> findByTimestampBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT m FROM ChatMessage m WHERE m.timestamp < ?1")
    List<ChatMessage> findOldMessages(LocalDateTime cutoffDate);
}