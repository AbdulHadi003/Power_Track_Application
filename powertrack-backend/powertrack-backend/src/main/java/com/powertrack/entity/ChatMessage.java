package com.powertrack.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.powertrack.enums.MessageType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonIgnore
    private ChatConversation conversation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    @JsonIgnore
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    @JsonIgnore
    private User receiver;

    // ✅ FIXED: Removed @NotBlank, made optional with max length
    @Size(max = 5000, message = "Message text cannot exceed 5000 characters")
    @Column(name = "message_text", columnDefinition = "TEXT")
    private String messageText;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false, length = 20)
    private MessageType messageType = MessageType.TEXT;

    @Column(name = "attachment_url", length = 500)
    private String attachmentUrl;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // ========== Helper Methods ==========

    public Long getConversationId() {
        return conversation != null ? conversation.getId() : null;
    }

    public Long getSenderId() {
        return sender != null ? sender.getId() : null;
    }

    public Long getReceiverId() {
        return receiver != null ? receiver.getId() : null;
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public boolean isSystemMessage() {
        return messageType == MessageType.SYSTEM;
    }

    // ✅ NEW: Business validation method (called from service)
    public void validate() {
        switch (messageType) {
            case TEXT:
                if (messageText == null || messageText.trim().isEmpty()) {
                    throw new IllegalArgumentException("Text messages must contain message text");
                }
                break;

            case IMAGE:
            case FILE:
                if (attachmentUrl == null || attachmentUrl.trim().isEmpty()) {
                    throw new IllegalArgumentException(
                            messageType + " messages must have an attachment URL"
                    );
                }
                break;

            case SYSTEM:
                // System messages can have optional text and attachment
                break;

            default:
                throw new IllegalArgumentException("Unknown message type: " + messageType);
        }
    }
}