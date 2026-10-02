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
import java.util.List;

import java.time.LocalDateTime;

@Service
public class ConversationPersistenceService {

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

    public Message saveIncomingMessage(
            Conversation conversation,
            SenderType sender,
            MessageType messageType,
            String content) {

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(sender);
        message.setMessageType(messageType);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());



        return saveMessage(message, conversation);
    }

    public Message saveOutgoingMessage(
            Conversation conversation,
            String content) {

        Message message = new Message();

        message.setConversation(conversation);
        message.setSender(SenderType.SYSTEM);
        message.setMessageType(MessageType.TEXT);
        message.setContent(content);
        message.setCreatedAt(LocalDateTime.now());

        return saveMessage(message, conversation);
    }

    public void updateConversationState(
            Conversation conversation,
            ConversationState state) {

        conversation.setState(state);
        conversation.setUpdatedAt(LocalDateTime.now());

        conversationRepository.save(conversation);
    }

    private Message saveMessage(
            Message message,
            Conversation conversation) {

        conversation.setUpdatedAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        return messageRepository.save(message);
    }
    public List<Message> getMessagesForConversation(
            Conversation conversation) {

        return messageRepository
                .findByConversationOrderByCreatedAtAsc(conversation);
    }
}