package com.aistetic.conversationallayer.dto;

import com.aistetic.conversationallayer.domain.MessageType;
import com.aistetic.conversationallayer.domain.SenderType;

import java.time.LocalDateTime;

public class ConversationActivity {

    private Long messageId;
    private SenderType sender;
    private MessageType messageType;
    private String content;
    private LocalDateTime createdAt;

    public ConversationActivity() {
    }

    public ConversationActivity(
            Long messageId,
            SenderType sender,
            MessageType messageType,
            String content,
            LocalDateTime createdAt) {

        this.messageId = messageId;
        this.sender = sender;
        this.messageType = messageType;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public SenderType getSender() {
        return sender;
    }

    public void setSender(SenderType sender) {
        this.sender = sender;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}