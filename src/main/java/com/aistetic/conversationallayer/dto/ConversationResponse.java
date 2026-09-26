package com.aistetic.conversationallayer.dto;
import com.aistetic.conversationallayer.domain.ConversationState;
public class ConversationResponse {
    private ConversationState state;
    private String message;

    public ConversationResponse(ConversationState state, String message) {
        this.state = state;
        this.message = message;
    }
    public ConversationState getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }
}
