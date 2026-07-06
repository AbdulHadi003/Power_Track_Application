package com.powertrack.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    private Long id;
    private Long conversationId;
    private Long senderId;
    private String senderName;
    private String senderRole;
    private Long receiverId;
    private String receiverName;
    private String messageText;
    private String messageType;
    private String attachmentUrl;
    private LocalDateTime timestamp;
    private Boolean isRead;
}