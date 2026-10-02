package com.aistetic.conversationallayer.repository;

import com.aistetic.conversationallayer.domain.Conversation;
import com.aistetic.conversationallayer.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByConversationOrderByCreatedAtAsc(
            Conversation conversation
    );
}