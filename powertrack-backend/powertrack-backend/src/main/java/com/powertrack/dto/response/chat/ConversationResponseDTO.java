package com.powertrack.dto.response.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponseDTO {

    private Long id;
    private Long userId;
    private String userName;
    private Long csrId;
    private String csrName;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private LocalDateTime lastMessageAt;
    private List<ChatMessageDTO> recentMessages;
    private Integer unreadCount;
}