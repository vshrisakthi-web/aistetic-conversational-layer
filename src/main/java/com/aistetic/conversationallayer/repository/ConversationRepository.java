package com.aistetic.conversationallayer.repository;

import com.aistetic.conversationallayer.domain.Conversation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConversationRepository extends JpaRepository<Conversation, Long> {
}