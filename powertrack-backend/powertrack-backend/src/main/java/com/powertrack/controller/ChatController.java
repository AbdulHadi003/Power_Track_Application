package com.powertrack.controller;

import com.powertrack.dto.request.chat.SendMessageRequest;
import com.powertrack.dto.request.chat.StartConversationRequest;
import com.powertrack.dto.response.chat.ChatMessageDTO;
import com.powertrack.dto.response.chat.ConversationResponseDTO;
import com.powertrack.service.ChatService;
import com.powertrack.util.SecurityUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final SecurityUtils securityUtils;

    @PostMapping("/conversations/start")
    public ResponseEntity<ConversationResponseDTO> startConversation(
            @Valid @RequestBody StartConversationRequest request) {
        Long userId = securityUtils.getCurrentUserId();
        ConversationResponseDTO conversation = chatService.startConversation(request, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(conversation);
    }

    @PostMapping("/messages/send")
    public ResponseEntity<ChatMessageDTO> sendMessage(@Valid @RequestBody SendMessageRequest request) {
        Long senderId = securityUtils.getCurrentUserId();
        ChatMessageDTO message = chatService.sendMessage(request, senderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/conversations/my-conversations")
    public ResponseEntity<List<ConversationResponseDTO>> getMyConversations() {
        Long userId = securityUtils.getCurrentUserId();
        List<ConversationResponseDTO> conversations = chatService.getMyConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversations/{id}")
    public ResponseEntity<ConversationResponseDTO> getConversationDetails(@PathVariable Long id) {
        ConversationResponseDTO conversation = chatService.getConversationDetails(id);
        return ResponseEntity.ok(conversation);
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<ChatMessageDTO>> getConversationMessages(@PathVariable Long id) {
        List<ChatMessageDTO> messages = chatService.getConversationMessages(id);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/messages/unread")
    public ResponseEntity<List<ChatMessageDTO>> getUnreadMessages() {
        Long userId = securityUtils.getCurrentUserId();
        List<ChatMessageDTO> messages = chatService.getUnreadMessages(userId);
        return ResponseEntity.ok(messages);
    }

    @PutMapping("/conversations/{id}/mark-read")
    public ResponseEntity<String> markMessagesAsRead(@PathVariable Long id) {
        Long userId = securityUtils.getCurrentUserId();
        chatService.markMessagesAsRead(id, userId);
        return ResponseEntity.ok("Messages marked as read");
    }

    @GetMapping("/messages/unread/count")
    public ResponseEntity<Long> countUnreadMessages() {
        Long userId = securityUtils.getCurrentUserId();
        long count = chatService.countUnreadMessages(userId);
        return ResponseEntity.ok(count);
    }

    // Support (CSR) endpoints

    @GetMapping("/conversations/managed")
    @PreAuthorize("hasRole('SUPPORT')")
    public ResponseEntity<List<ConversationResponseDTO>> getManagedConversations() {
        Long csrId = securityUtils.getCurrentUserId();
        List<ConversationResponseDTO> conversations = chatService.getManagedConversations(csrId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/conversations/waiting")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<List<ConversationResponseDTO>> getWaitingConversations() {
        List<ConversationResponseDTO> conversations = chatService.getWaitingConversations();
        return ResponseEntity.ok(conversations);
    }

    @PutMapping("/conversations/{id}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<ConversationResponseDTO> assignCSRToConversation(
            @PathVariable Long id,
            @RequestParam Long csrId) {
        ConversationResponseDTO conversation = chatService.assignCSRToConversation(id, csrId);
        return ResponseEntity.ok(conversation);
    }

    @PutMapping("/conversations/{id}/close")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<ConversationResponseDTO> closeConversation(
            @PathVariable Long id,
            @RequestParam String closedBy) {
        ConversationResponseDTO conversation = chatService.closeConversation(id, closedBy);
        return ResponseEntity.ok(conversation);
    }

    // Admin endpoints

    @GetMapping("/conversations/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ConversationResponseDTO>> getActiveConversations() {
        List<ConversationResponseDTO> conversations = chatService.getActiveConversations();
        return ResponseEntity.ok(conversations);
    }

    @DeleteMapping("/messages/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        chatService.deleteMessage(id);
        return ResponseEntity.ok("Message deleted successfully");
    }

    @GetMapping("/conversations/count/active")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Long> countActiveConversations() {
        long count = chatService.countActiveConversations();
        return ResponseEntity.ok(count);
    }
}