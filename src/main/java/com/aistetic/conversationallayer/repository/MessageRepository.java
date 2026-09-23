package com.aistetic.conversationallayer.repository;

import com.aistetic.conversationallayer.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}