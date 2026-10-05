package com.aistetic.conversationallayer.controller;

import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.domain.MessageType;
import com.aistetic.conversationallayer.domain.SenderType;
import com.aistetic.conversationallayer.domain.ConversationState;
import com.aistetic.conversationallayer.dto.ConversationResponse;
import com.aistetic.conversationallayer.orchestrator.ConversationOrchestrator;
import com.aistetic.conversationallayer.service.ConversationContext;
import com.aistetic.conversationallayer.service.ConversationContextStore;
import com.aistetic.conversationallayer.service.WhatsAppMessageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.aistetic.conversationallayer.domain.Conversation;
import com.aistetic.conversationallayer.domain.User;
import com.aistetic.conversationallayer.service.ConversationPersistenceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.UUID;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/whatsapp")
public class WhatsAppWebhookController {

    private static final Logger logger =
            LoggerFactory.getLogger(WhatsAppWebhookController.class);

    @Value("${whatsapp.verify-token}")
    private String verifyToken;

    private final ConversationOrchestrator orchestrator;
    private final ConversationContextStore contextStore;
    private final WhatsAppMessageService whatsappMessageService;
    private final ConversationPersistenceService persistenceService;

    public WhatsAppWebhookController(
            ConversationOrchestrator orchestrator,
            ConversationContextStore contextStore,
            WhatsAppMessageService whatsappMessageService,
            ConversationPersistenceService persistenceService) {

        this.orchestrator = orchestrator;
        this.contextStore = contextStore;
        this.whatsappMessageService = whatsappMessageService;
        this.persistenceService = persistenceService;
    }

    @GetMapping
    public ResponseEntity<String> verifyWebhook(
            @RequestParam("hub.mode") String mode,
            @RequestParam("hub.verify_token") String token,
            @RequestParam("hub.challenge") String challenge) {

        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        }

        return ResponseEntity.status(403).body("Verification failed");
    }

    @PostMapping
    public ResponseEntity<String> receiveWhatsAppWebhook(
            @RequestBody Map<String, Object> payload) {

        String correlationId = "CORR-" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        MDC.put("correlationId", correlationId);

        logger.info(
                "WhatsApp webhook received. correlationId={}",
                correlationId
        );

        try {

            // ---------------------------------------------------------
            // 1. Extract entry
            // ---------------------------------------------------------

            List<?> entries = (List<?>) payload.get("entry");

            if (entries == null || entries.isEmpty()) {
                return ResponseEntity.ok("EVENT_RECEIVED");
            }

            Map<?, ?> entry = (Map<?, ?>) entries.get(0);

            // ---------------------------------------------------------
            // 2. Extract changes
            // ---------------------------------------------------------

            List<?> changes = (List<?>) entry.get("changes");

            if (changes == null || changes.isEmpty()) {
                return ResponseEntity.ok("EVENT_RECEIVED");
            }

            Map<?, ?> change = (Map<?, ?>) changes.get(0);

            // ---------------------------------------------------------
            // 3. Extract value
            // ---------------------------------------------------------

            Map<?, ?> value = (Map<?, ?>) change.get("value");

            if (value == null) {
                return ResponseEntity.ok("EVENT_RECEIVED");
            }

            // ---------------------------------------------------------
            // 4. Extract messages
            // ---------------------------------------------------------

            List<?> messages = (List<?>) value.get("messages");

            if (messages == null || messages.isEmpty()) {
                return ResponseEntity.ok("EVENT_RECEIVED");
            }

            Map<?, ?> whatsappMessage =
                    (Map<?, ?>) messages.get(0);

            // ---------------------------------------------------------
            // 5. Extract WhatsApp message ID
            // ---------------------------------------------------------

            String whatsappMessageId =
                    String.valueOf(whatsappMessage.get("id"));

            logger.info(
                    "WhatsApp message received with messageId={}",
                    whatsappMessageId
            );

        // ---------------------------------------------------------
        // 6. Prevent duplicate webhook processing
        // ---------------------------------------------------------

            if (whatsappMessageId == null
                    || "null".equals(whatsappMessageId)
                    || whatsappMessageId.isBlank()) {

                System.out.println(
                        "WhatsApp message ID is missing."
                );

            } else if (
                    persistenceService.messageAlreadyProcessed(
                            whatsappMessageId
                    )
            ) {

                logger.info(
                        "Duplicate WhatsApp message ignored. messageId={}",
                        whatsappMessageId
                );

                return ResponseEntity.ok("EVENT_RECEIVED");
            }

            // ---------------------------------------------------------
            // 7. Extract sender
            // ---------------------------------------------------------

            String waId = String.valueOf(
                    whatsappMessage.get("from")
            );

            logger.info("WhatsApp message received from sender");

            // ---------------------------------------------------------
            // 8. Extract message type
            // ---------------------------------------------------------

            String type = String.valueOf(
                    whatsappMessage.get("type")
            );

            logger.info("WhatsApp message type={}", type);

            // ---------------------------------------------------------
            // 9. Create conversation context
            // ---------------------------------------------------------

            Long whatsappUserId = Long.parseLong(waId);

            ConversationContext context =
                    contextStore.getOrCreate(whatsappUserId);

            User user =
                    persistenceService.getOrCreateUser(waId);

            Conversation conversation =
                    persistenceService.getOrCreateConversation(user);

            context.setConversationId(conversation.getId());
            context.setUserId(user.getId());

            logger.info(
                    "Conversation loaded. conversationId={}, state={}",
                    conversation.getId(),
                    conversation.getState()
            );

            // ---------------------------------------------------------
            // 10. Create Message object
            // ---------------------------------------------------------

            Message message = new Message();

            message.setSender(SenderType.USER);

            // ---------------------------------------------------------
            // 9. Handle TEXT message
            // ---------------------------------------------------------

            if ("text".equals(type)) {

                Map<?, ?> text =
                        (Map<?, ?>) whatsappMessage.get("text");

                String content = String.valueOf(
                        text.get("body")
                );

                message.setMessageType(MessageType.TEXT);
                message.setContent(content);

                logger.info("WhatsApp text message received");
            }

            // ---------------------------------------------------------
            // 10. Handle IMAGE message
            // ---------------------------------------------------------

            else if ("image".equals(type)) {

                Map<?, ?> image =
                        (Map<?, ?>) whatsappMessage.get("image");

                String mediaId = String.valueOf(
                        image.get("id")
                );

                message.setMessageType(MessageType.IMAGE);
                message.setContent(mediaId);

                logger.info("WhatsApp image message received");
            }

            // ---------------------------------------------------------
            // 11. Ignore unsupported message types for now
            // ---------------------------------------------------------

            else {

                logger.warn(
                        "Unsupported WhatsApp message type={}",
                        type
                );

                return ResponseEntity.ok("EVENT_RECEIVED");
            }
            persistenceService.saveIncomingMessage(
                    conversation,
                    message.getSender(),
                    message.getMessageType(),
                    message.getContent(),
                    whatsappMessageId
            );

            // ---------------------------------------------------------
            // 12. Send message to ConversationOrchestrator
            // ---------------------------------------------------------

            ConversationResponse response =
                    orchestrator.process(
                            context,
                            message
                    );

            // WhatsApp image:
            // First call moves NEW -> IMAGE_RECEIVED.
            // Second call continues the existing conversation flow
            // from IMAGE_RECEIVED -> listing generation.
            if ("image".equals(type)
                    && response.getState() == ConversationState.IMAGE_RECEIVED) {

                response = orchestrator.process(
                        context,
                        message
                );
            }

            // ---------------------------------------------------------
            // 13. Print orchestrator response
            // ---------------------------------------------------------
            persistenceService.updateConversationState(
                    conversation,
                    response.getState()
            );
            logger.info(
                    "Conversation state changed to {}",
                    response.getState()
            );

            logger.info(
                    "Conversation response generated. state={}",
                    response.getState()
            );


            // ---------------------------------------------------------
            // 14. Send response back to WhatsApp
            // ---------------------------------------------------------
            persistenceService.saveOutgoingMessage(
                    conversation,
                    response.getMessage()
            );
            whatsappMessageService.sendTextMessage(
                    waId,
                    response.getMessage()
            );

        } catch (Exception e) {

            logger.error(
                    "Error processing WhatsApp webhook",
                    e
            );

        } finally {

            MDC.remove("correlationId");
        }

        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}