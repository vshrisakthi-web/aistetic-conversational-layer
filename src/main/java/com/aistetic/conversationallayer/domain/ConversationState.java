package com.aistetic.conversationallayer.domain;

public enum ConversationState {

    NEW,
    IMAGE_RECEIVED,
    PROCESSING,
    LISTING_READY,
    AWAITING_APPROVAL,
    AWAITING_MARKETPLACE,
    PUBLISHING,
    PUBLISHED,
    FAILED
}