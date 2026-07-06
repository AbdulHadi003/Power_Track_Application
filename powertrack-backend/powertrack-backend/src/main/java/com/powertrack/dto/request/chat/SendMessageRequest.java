package com.powertrack.dto.request.chat;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {

    @NotNull(message = "Conversation ID is required")
    private Long conversationId;

    @Size(max = 5000, message = "Message text cannot exceed 5000 characters")
    private String messageText; // Optional for non-TEXT types

    @Pattern(regexp = "TEXT|IMAGE|FILE|SYSTEM", message = "Invalid message type")
    private String messageType = "TEXT";

    @Size(max = 500, message = "Attachment URL cannot exceed 500 characters")
    private String attachmentUrl; // Required for IMAGE/FILE types
}