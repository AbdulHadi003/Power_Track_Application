package com.powertrack.service;

import com.powertrack.dto.mapper.ChatMapper;
import com.powertrack.dto.request.chat.SendMessageRequest;
import com.powertrack.dto.request.chat.StartConversationRequest;
import com.powertrack.dto.response.chat.ChatMessageDTO;
import com.powertrack.dto.response.chat.ConversationResponseDTO;
import com.powertrack.entity.ChatConversation;
import com.powertrack.entity.ChatMessage;
import com.powertrack.entity.User;
import com.powertrack.enums.ConversationStatus;
import com.powertrack.enums.MessageType;
import com.powertrack.exception.ResourceNotFoundException;
import com.powertrack.repository.ChatConversationRepository;
import com.powertrack.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatConversationRepository conversationRepository;
    private final ChatMessageRepository messageRepository;
    private final UserService userService;

    @Transactional
    public ConversationResponseDTO startConversation(StartConversationRequest request, Long userId) {
        User user = userService.getUserEntityById(userId);

        // Check if user already has an active conversation
        var existingConversation = conversationRepository
                .findByUserIdAndStatus(userId, ConversationStatus.ACTIVE);

        if (existingConversation.isPresent()) {
            return getConversationDetails(existingConversation.get().getId());
        }

        // Create new conversation
        ChatConversation conversation = new ChatConversation();
        conversation.setUser(user);
        conversation.setStatus(ConversationStatus.ACTIVE);
        conversation.setLastMessageAt(LocalDateTime.now());

        ChatConversation savedConversation = conversationRepository.save(conversation);

        // Send initial message if provided
        if (request.getInitialMessage() != null && !request.getInitialMessage().trim().isEmpty()) {
            SendMessageRequest messageRequest = new SendMessageRequest();
            messageRequest.setConversationId(savedConversation.getId());
            messageRequest.setMessageText(request.getInitialMessage());
            messageRequest.setMessageType("TEXT");
            sendMessage(messageRequest, userId);
        }

        return getConversationDetails(savedConversation.getId());
    }

    @Transactional
    public ChatMessageDTO sendMessage(SendMessageRequest request, Long senderId) {
        ChatConversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", request.getConversationId()));

        User sender = userService.getUserEntityById(senderId);

        // Determine receiver
        User receiver;
        if (sender.getId().equals(conversation.getUser().getId())) {
            // Customer sending to CSR
            receiver = conversation.getCsr();
        } else {
            // CSR sending to customer
            receiver = conversation.getUser();
        }

        // Create message
        ChatMessage message = new ChatMessage();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setMessageText(request.getMessageText());
        message.setMessageType(MessageType.valueOf(request.getMessageType()));
        message.setAttachmentUrl(request.getAttachmentUrl());
        message.setIsRead(false);

        // Validate message
        message.validate();

        ChatMessage savedMessage = messageRepository.save(message);

        // Update conversation's last message time
        conversation.updateLastMessageTime();
        conversationRepository.save(conversation);

        return ChatMapper.toMessageDTO(savedMessage);
    }

    @Transactional
    public ConversationResponseDTO assignCSRToConversation(Long conversationId, Long csrId) {
        ChatConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));

        User csr = userService.getUserEntityById(csrId);

        conversation.setCsr(csr);
        conversationRepository.save(conversation);

        return getConversationDetails(conversationId);
    }

    @Transactional
    public ConversationResponseDTO closeConversation(Long conversationId, String closedBy) {
        ChatConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));

        conversation.setStatus(ConversationStatus.CLOSED);
        conversation.setClosedBy(closedBy);
        conversation.setEndedAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        return getConversationDetails(conversationId);
    }

    @Transactional(readOnly = true)
    public ConversationResponseDTO getConversationDetails(Long conversationId) {
        ChatConversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", conversationId));

        List<ChatMessage> recentMessages = messageRepository.findRecentMessages(conversationId);
        Integer unreadCount = (int) messageRepository.countUnreadMessages(conversation.getUser().getId());

        return ChatMapper.toConversationDTO(conversation, recentMessages, unreadCount);
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDTO> getMyConversations(Long userId) {
        List<ChatConversation> conversations = conversationRepository.findByUserId(userId);

        return conversations.stream()
                .map(conv -> {
                    List<ChatMessage> recentMessages = messageRepository.findRecentMessages(conv.getId());
                    int unreadCount = (int) messageRepository.countUnreadMessages(userId);
                    return ChatMapper.toConversationDTO(conv, recentMessages, unreadCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDTO> getManagedConversations(Long csrId) {
        List<ChatConversation> conversations = conversationRepository.findByCsrId(csrId);

        return conversations.stream()
                .map(conv -> {
                    List<ChatMessage> recentMessages = messageRepository.findRecentMessages(conv.getId());
                    int unreadCount = (int) messageRepository.countUnreadMessages(csrId);
                    return ChatMapper.toConversationDTO(conv, recentMessages, unreadCount);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDTO> getActiveConversations() {
        List<ChatConversation> conversations = conversationRepository.findAllActiveConversations();

        return conversations.stream()
                .map(conv -> {
                    List<ChatMessage> recentMessages = messageRepository.findRecentMessages(conv.getId());
                    return ChatMapper.toConversationDTO(conv, recentMessages, 0);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ConversationResponseDTO> getWaitingConversations() {
        List<ChatConversation> conversations = conversationRepository.findWaitingConversations();

        return conversations.stream()
                .map(conv -> {
                    List<ChatMessage> recentMessages = messageRepository.findRecentMessages(conv.getId());
                    return ChatMapper.toConversationDTO(conv, recentMessages, 0);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getConversationMessages(Long conversationId) {
        return messageRepository.findConversationMessages(conversationId).stream()
                .map(ChatMapper::toMessageDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ChatMessageDTO> getUnreadMessages(Long userId) {
        return messageRepository.findUnreadMessages(userId).stream()
                .map(ChatMapper::toMessageDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markMessagesAsRead(Long conversationId, Long userId) {
        List<ChatMessage> unreadMessages = messageRepository.findUnreadMessages(userId).stream()
                .filter(msg -> msg.getConversation().getId().equals(conversationId))
                .collect(Collectors.toList());

        unreadMessages.forEach(msg -> msg.setIsRead(true));
        messageRepository.saveAll(unreadMessages);
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        ChatMessage message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        message.softDelete();
        messageRepository.save(message);
    }

    @Transactional(readOnly = true)
    public long countUnreadMessages(Long userId) {
        return messageRepository.countUnreadMessages(userId);
    }

    @Transactional(readOnly = true)
    public long countActiveConversations() {
        return conversationRepository.countByCsrIdAndStatus(null, ConversationStatus.ACTIVE);
    }
}