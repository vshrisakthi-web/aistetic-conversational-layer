package com.aistetic.conversationallayer.orchestrator;

import com.aistetic.conversationallayer.domain.ConversationState;
import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.domain.MessageType;
import com.aistetic.conversationallayer.dto.ConversationResponse;
import com.aistetic.conversationallayer.service.ConversationContext;
import com.aistetic.conversationallayer.service.IntentDetectionService;
import com.aistetic.conversationallayer.service.ListingService;
import com.aistetic.conversationallayer.service.PublishingService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

class ConversationOrchestratorTest {

    private final IntentDetectionService intentDetectionService =
            new IntentDetectionService();

    private final ListingService listingService =
            mock(ListingService.class);

    private final PublishingService publishingService =
            mock(PublishingService.class);

    private final ConversationOrchestrator orchestrator =
            new ConversationOrchestrator(
                    intentDetectionService,
                    listingService,
                    publishingService
            );

    @Test
    void shouldMoveNewConversationToImageReceived() {

        ConversationContext context =
                new ConversationContext();

        Message message = new Message();
        message.setMessageType(MessageType.IMAGE);

        ConversationResponse response =
                orchestrator.process(context, message);

        assertEquals(
                ConversationState.IMAGE_RECEIVED,
                response.getState()
        );

        assertEquals(
                "Image received.",
                response.getMessage()
        );
    }

    @Test
    void shouldMoveImageReceivedToProcessing() {

        ConversationContext context =
                new ConversationContext();

        context.setCurrentState(
                ConversationState.IMAGE_RECEIVED
        );

        Message message = new Message();
        message.setMessageType(MessageType.IMAGE);

        ConversationResponse response =
                orchestrator.process(context, message);

        assertEquals(
                ConversationState.PROCESSING,
                response.getState()
        );
    }

    @Test
    void shouldMoveListingReadyToAwaitingApproval() {

        ConversationContext context =
                new ConversationContext();

        context.setCurrentState(
                ConversationState.LISTING_READY
        );

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("anything");

        ConversationResponse response =
                orchestrator.process(context, message);

        assertEquals(
                ConversationState.AWAITING_APPROVAL,
                response.getState()
        );
    }

    @Test
    void shouldApproveListingAndAskForMarketplace() {

        ConversationContext context =
                new ConversationContext();

        context.setCurrentState(
                ConversationState.AWAITING_APPROVAL
        );

        context.setCurrentListingId(5L);

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("yes");

        ConversationResponse response =
                orchestrator.process(context, message);

        assertEquals(
                ConversationState.AWAITING_MARKETPLACE,
                response.getState()
        );

        assertEquals(
                "Listing approved. Where would you like to publish?",
                response.getMessage()
        );

        verify(listingService).approveListing(5L);
    }

    @Test
    void shouldSelectAllMarketplacesAndMoveToPublishing() {

        ConversationContext context =
                new ConversationContext();

        context.setCurrentState(
                ConversationState.AWAITING_MARKETPLACE
        );

        Message message = new Message();
        message.setMessageType(MessageType.TEXT);
        message.setContent("4");

        ConversationResponse response =
                orchestrator.process(context, message);

        assertEquals(
                ConversationState.PUBLISHING,
                response.getState()
        );

        assertEquals(
                List.of("EBAY", "VINTED", "DEPOP"),
                context.getSelectedMarketplaces()
        );
    }
}