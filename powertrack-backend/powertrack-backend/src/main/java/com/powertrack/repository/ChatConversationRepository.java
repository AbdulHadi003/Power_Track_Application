package com.powertrack.repository;

import com.powertrack.entity.ChatConversation;
import com.powertrack.enums.ConversationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatConversationRepository extends JpaRepository< ChatConversation, Long> {

    List<ChatConversation> findByUserId(Long userId);

    List<ChatConversation> findByCsrId(Long csrId);

    List<ChatConversation> findByStatus(ConversationStatus status);

    Optional<ChatConversation> findByUserIdAndStatus(Long userId, ConversationStatus status);

    List<ChatConversation> findByCsrIdAndStatus(Long csrId, ConversationStatus status);

    @Query("SELECT c FROM ChatConversation c WHERE c.status = 'ACTIVE' ORDER BY c.lastMessageAt DESC")
    List<ChatConversation> findAllActiveConversations();

    @Query("SELECT c FROM ChatConversation c WHERE c.csr IS NULL AND c.status = 'ACTIVE' ")
    List<ChatConversation> findWaitingConversations();

    long countByCsrIdAndStatus(Long csrId, ConversationStatus status);

    @Query("SELECT c FROM ChatConversation c ORDER BY c.lastMessageAt DESC")
    List<ChatConversation> findRecentConversations();
}