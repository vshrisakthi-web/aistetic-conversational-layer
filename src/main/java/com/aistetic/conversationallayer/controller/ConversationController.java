package com.aistetic.conversationallayer.controller;

import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.domain.MessageType;
import com.aistetic.conversationallayer.dto.ConversationMessageRequest;
import com.aistetic.conversationallayer.dto.ConversationResponse;
import com.aistetic.conversationallayer.orchestrator.ConversationOrchestrator;
import com.aistetic.conversationallayer.service.ConversationContext;
import com.aistetic.conversationallayer.service.ConversationContextStore;
import org.springframework.web.bind.annotation.*;
import com.aistetic.conversationallayer.service.ConversationActivityService;
import com.aistetic.conversationallayer.dto.ConversationActivity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/conversations")
public class ConversationController {

    private final ConversationOrchestrator orchestrator;
    private final ConversationContextStore contextStore;
    private final ConversationActivityService conversationActivityService;

    public ConversationController(
            ConversationOrchestrator orchestrator,
            ConversationContextStore contextStore,
            ConversationActivityService conversationActivityService) {


        this.orchestrator = orchestrator;
        this.contextStore = contextStore;
        this.conversationActivityService =
                conversationActivityService;
    }

    @PostMapping("/message")
    public ConversationResponse processMessage(
            @RequestBody ConversationMessageRequest request) {

        ConversationContext context =
                contextStore.getOrCreate(
                        request.getConversationId()
                );

        Message message = new Message();

        MessageType messageType;

        try {
            messageType = MessageType.valueOf(
                    request.getMessageType().toUpperCase()
            );
        } catch (Exception e) {
            messageType = MessageType.TEXT;
        }

        message.setMessageType(messageType);
        message.setContent(request.getMessage());

        return orchestrator.process(
                context,
                message
        );
    }
    @GetMapping("/{conversationId}/messages")
    public List<ConversationActivity> getConversationActivity(
            @PathVariable Long conversationId) {

        return conversationActivityService
                .getConversationActivity(conversationId);
    }

}