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

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/whatsapp")
public class WhatsAppWebhookController {

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

        System.out.println("WhatsApp webhook received:");
        System.out.println(payload);

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
            // 5. Extract sender
            // ---------------------------------------------------------

            String waId = String.valueOf(
                    whatsappMessage.get("from")
            );

            System.out.println("WhatsApp sender: " + waId);

            // ---------------------------------------------------------
            // 6. Extract message type
            // ---------------------------------------------------------

            String type = String.valueOf(
                    whatsappMessage.get("type")
            );

            System.out.println("WhatsApp message type: " + type);

            // ---------------------------------------------------------
            // 7. Create conversation context
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

            // ---------------------------------------------------------
            // 8. Create Message object
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

                System.out.println(
                        "WhatsApp text: " + content
                );
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

                System.out.println(
                        "WhatsApp image media ID: " + mediaId
                );
            }

            // ---------------------------------------------------------
            // 11. Ignore unsupported message types for now
            // ---------------------------------------------------------

            else {

                System.out.println(
                        "Unsupported WhatsApp message type: " + type
                );

                return ResponseEntity.ok("EVENT_RECEIVED");
            }
            persistenceService.saveIncomingMessage(
                    conversation,
                    message.getSender(),
                    message.getMessageType(),
                    message.getContent()
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
            System.out.println(
                    "Conversation state: "
                            + response.getState()
            );

            System.out.println(
                    "Conversation response: "
                            + response.getMessage()
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

            System.out.println(
                    "Error processing WhatsApp webhook:"
            );

            e.printStackTrace();
        }

        return ResponseEntity.ok("EVENT_RECEIVED");
    }
}