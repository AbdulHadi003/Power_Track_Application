package com.powertrack.dto.request.chat;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartConversationRequest {

    @Size(max = 500, message = "Initial message cannot exceed 500 characters")
    private String initialMessage; // Optional - first message when starting chat
}