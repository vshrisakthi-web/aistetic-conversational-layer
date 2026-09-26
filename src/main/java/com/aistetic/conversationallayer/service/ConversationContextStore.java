package com.aistetic.conversationallayer.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationContextStore {

    private final Map<Long, ConversationContext> contexts =
            new ConcurrentHashMap<>();

    public ConversationContext getOrCreate(Long conversationId) {

        return contexts.computeIfAbsent(
                conversationId,
                id -> new ConversationContext()
        );
    }
}