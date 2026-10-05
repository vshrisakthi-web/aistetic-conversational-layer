package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.domain.Conversation;
import com.aistetic.conversationallayer.domain.ConversationState;
import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.domain.MessageType;
import com.aistetic.conversationallayer.domain.SenderType;
import com.aistetic.conversationallayer.domain.User;
import com.aistetic.conversationallayer.repository.ConversationRepository;
import com.aistetic.conversationallayer.repository.MessageRepository;
import com.aistetic.conversationallayer.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ConversationPersistenceService {

    private static final Logger logger =
            LoggerFactory.getLogger(ConversationPersistenceService.class);

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ConversationPersistenceService(
            UserRepository userRepository,
            ConversationRepository conversationRepository,
            MessageRepository messageRepository) {

        this.userRepository = userRepository;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    public User getOrCreateUser(String phoneNumber) {

        return userRepository.findByPhoneNumber(phoneNumber)
                .orElseGet(() -> {

                    User user = new User();

                    user.setPhoneNumber(phoneNumber);
                    user.setName("WhatsApp User");
                    user.setCreatedAt(LocalDateTime.now());
                    user.setUpdatedAt(LocalDateTime.now());

                    return userRepository.save(user);
                });
    }

    public Conversation getOrCreateConversation(User user) {

        return conversationRepository
                .findFirstByUserOrderByUpdatedAtDesc(user)
                .orElseGet(() -> {

                    Conversation conversation = new Conversation();

                    conversation.setUser(user);
                    conversation.setState(ConversationState.NEW);
                    conversation.setCreatedAt(LocalDateTime.now());
                    conversation.setUpdatedAt(LocalDateTime.now());

                    return conversationRepository.save(conversation);
                });
    }

    // ---------------------------------------------------------
    // Check whether WhatsApp message was already processed
    // ---------------------------------------------------------

    public boolean messageAlreadyProcessed(String whatsappMessageId) {

        if (whatsappMessageId == null || whatsappMessageId.isBlank()) {
            return false;
        }

        boolean exists =
                messageRepository.existsByWhatsappMessageId(
                        whatsappMessageId
                );

        logger.debug(
                "Checked WhatsApp message idempotency. exists={}",
                exists
        );

        return exists;
    }

    // ---------------------------------------------------------
    // Save incoming WhatsApp message
    // ---------------------------------------------------------

    public Message saveIncomingMessage(
            Conversation conversation,
            SenderType sender,
            MessageType messageType,
            String content,
            String whatsappMessageId) {

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setWhatsappMessageId(whatsappMessageId);
        message.setCreatedAt(LocalDateTime.now());

        logger.info(
                "Saving incoming message. type={}",
                messageType
        );

        return saveMessage(message, conversation);
    }

    // ---------------------------------------------------------
    // Backward-compatible method
    // ---------------------------------------------------------

    public Message saveIncomingMessage(
            Conversation conversation,
            SenderType sender,
            MessageType messageType,
            String content) {

        return saveIncomingMessage(
                conversation,
                sender,
                messageType,
                content,
                null
        );
    }

    // ---------------------------------------------------------
    // Save outgoing message
    // ---------------------------------------------------------

    public Message saveOutgoingMessage(
            Conversation conversation,
            String content) {
        logger.info("Saving outgoing system message");

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(SenderType.SYSTEM);
        message.setMessageType(MessageType.TEXT);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());

        return saveMessage(message, conversation);
    }

    // ---------------------------------------------------------
    // Update conversation state
    // ---------------------------------------------------------

    public void updateConversationState(
            Conversation conversation,
            ConversationState state) {
        logger.info(
                "Updating conversation state. conversationId={}, newState={}",
                conversation.getId(),
                state
        );

        conversation.setState(state);
        conversation.setUpdatedAt(LocalDateTime.now());

        conversationRepository.save(conversation);
    }

    // ---------------------------------------------------------
    // Common message save method
    // ---------------------------------------------------------

    private Message saveMessage(
            Message message,
            Conversation conversation) {

        conversation.setUpdatedAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        return messageRepository.save(message);
    }

    // ---------------------------------------------------------
    // Get conversation messages
    // ---------------------------------------------------------

    public List<Message> getMessagesForConversation(
            Conversation conversation) {

        return messageRepository
                .findByConversationOrderByCreatedAtAsc(conversation);
    }
}