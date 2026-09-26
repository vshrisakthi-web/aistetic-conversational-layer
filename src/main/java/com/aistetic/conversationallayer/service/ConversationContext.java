package com.aistetic.conversationallayer.service;


import com.aistetic.conversationallayer.domain.ConversationState;
import java.util.List;

public class ConversationContext {
    private Long conversationId;
    private Long userId;
    private ConversationState currentState = ConversationState.NEW;
    private Long currentListingId;
    private List<String> selectedMarketplaces;

    //Add Getters and Setters
    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
    //......
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    //....
    public ConversationState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(ConversationState currentState) {
        this.currentState = currentState;
    }
    //..
    public Long getCurrentListingId() {
        return currentListingId;
    }

    public void setCurrentListingId(Long currentListingId) {
        this.currentListingId = currentListingId;
    }

    public List<String> getSelectedMarketplaces() {
        return selectedMarketplaces;
    }

    public void setSelectedMarketplaces(List<String> selectedMarketplaces) {
        this.selectedMarketplaces = selectedMarketplaces;
    }
}
