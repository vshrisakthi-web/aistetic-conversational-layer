package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.domain.Conversation;
import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.dto.ConversationActivity;
import com.aistetic.conversationallayer.repository.ConversationRepository;
import org.springframework.stereotype.Service;
import com.aistetic.conversationallayer.exception.ConversationNotFoundException;

import java.util.List;

@Service
public class ConversationActivityService {

    private final ConversationRepository conversationRepository;
    private final ConversationPersistenceService conversationPersistenceService;

    public ConversationActivityService(
            ConversationRepository conversationRepository,
            ConversationPersistenceService conversationPersistenceService) {

        this.conversationRepository = conversationRepository;
        this.conversationPersistenceService = conversationPersistenceService;
    }

    public List<ConversationActivity> getConversationActivity(
            Long conversationId) {

        Conversation conversation =
                conversationRepository.findById(conversationId)
                        .orElseThrow(() ->
                                new ConversationNotFoundException(conversationId)
                        );

        List<Message> messages =
                conversationPersistenceService
                        .getMessagesForConversation(conversation);

        return messages.stream()
                .map(message ->
                        new ConversationActivity(
                                message.getId(),
                                message.getSender(),
                                message.getMessageType(),
                                message.getContent(),
                                message.getCreatedAt()
                        )
                )
                .toList();
    }
}