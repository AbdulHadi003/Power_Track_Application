package com.powertrack.dto.mapper;

import com.powertrack.dto.response.chat.ChatMessageDTO;
import com.powertrack.dto.response.chat.ConversationResponseDTO;
import com.powertrack.entity.ChatConversation;
import com.powertrack.entity.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChatMapper {

    public static ConversationResponseDTO toConversationDTO(
            ChatConversation conversation,
            List<ChatMessage> recentMessages,
            Integer unreadCount
    ) {
        if (conversation == null) {
            return null;
        }

        List<ChatMessageDTO> dtoMessages = recentMessages != null
                ? recentMessages.stream()
                .map(ChatMapper::toMessageDTO)
                .collect(Collectors.toList())
                : null;

        return ConversationResponseDTO.builder()
                .id(conversation.getId())
                .userId(conversation.getUserId())
                .userName(conversation.getUser() != null ? conversation.getUser().getName() : null)
                .csrId(conversation.getCsrId())
                .csrName(conversation.getCsr() != null ? conversation.getCsr().getName() : null)
                .status(conversation.getStatus().name())
                .startedAt(conversation.getStartedAt())
                .endedAt(conversation.getEndedAt())
                .lastMessageAt(conversation.getLastMessageAt())
                .recentMessages(dtoMessages)
                .unreadCount(unreadCount)
                .build();
    }


    public static ChatMessageDTO toMessageDTO(ChatMessage message) {
        if (message == null) {
            return null;
        }

        return ChatMessageDTO.builder()
                .id(message.getId())
                .conversationId(message.getConversationId())
                .senderId(message.getSenderId())
                .senderName(message.getSender() != null ? message.getSender().getName() : null)
                .senderRole(message.getSender() != null ? message.getSender().getRole().name() : null)
                .receiverId(message.getReceiverId())
                .receiverName(message.getReceiver() != null ? message.getReceiver().getName() : null)
                .messageText(message.getMessageText())
                .messageType(message.getMessageType().name())
                .attachmentUrl(message.getAttachmentUrl())
                .timestamp(message.getTimestamp())
                .isRead(message.getIsRead())
                .build();
    }
}