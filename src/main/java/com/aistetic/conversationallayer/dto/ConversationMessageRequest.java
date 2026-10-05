package com.aistetic.conversationallayer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class ConversationMessageRequest {

    @NotNull(message = "conversationId is required")
    private Long conversationId;

    @NotBlank(message = "message is required")
    private String message;

    @NotBlank(message = "messageType is required")
    @Pattern(
            regexp = "TEXT|IMAGE|COMMAND|SYSTEM",
            flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "messageType must be TEXT, IMAGE, COMMAND, or SYSTEM"
    )
    private String messageType;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}