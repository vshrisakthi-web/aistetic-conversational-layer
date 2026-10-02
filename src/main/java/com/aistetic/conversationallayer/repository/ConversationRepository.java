package com.aistetic.conversationallayer.repository;

import com.aistetic.conversationallayer.domain.Conversation;
import com.aistetic.conversationallayer.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConversationRepository
        extends JpaRepository<Conversation, Long> {

    Optional<Conversation> findFirstByUserOrderByUpdatedAtDesc(User user);
}