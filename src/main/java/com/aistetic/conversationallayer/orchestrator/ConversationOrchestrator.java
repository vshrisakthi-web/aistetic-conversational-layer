package com.aistetic.conversationallayer.orchestrator;
import com.aistetic.conversationallayer.domain.ConversationState;
import com.aistetic.conversationallayer.domain.IntentType;
import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.dto.ConversationResponse;
import com.aistetic.conversationallayer.service.ConversationContext;
import com.aistetic.conversationallayer.service.IntentDetectionService;
import org.springframework.stereotype.Service;
import com.aistetic.conversationallayer.service.ListingService;
import java.util.List;

@Service
public class ConversationOrchestrator {
    private final IntentDetectionService intentDetectionService;
    private final ListingService listingService;
    public ConversationOrchestrator(
            IntentDetectionService intentDetectionService, ListingService listingService) {

        this.intentDetectionService = intentDetectionService;
        this.listingService = listingService;
    }
    public ConversationResponse process(
            ConversationContext context,
            Message message) {

        if (context == null || message == null) {
            return new ConversationResponse(
                    ConversationState.FAILED,
                    "Unable to process the conversation."
            );
        }

        IntentType intent = intentDetectionService.detectIntent(message);
        ConversationState currentState = context.getCurrentState();

        if (currentState == ConversationState.NEW) {

            if (intent == IntentType.UPLOAD_IMAGE) {

                context.setCurrentState(ConversationState.IMAGE_RECEIVED);

                return new ConversationResponse(
                        ConversationState.IMAGE_RECEIVED,
                        "Image received."
                );
            }

            if (intent == IntentType.HELP) {

                return new ConversationResponse(
                        ConversationState.NEW,
                        "Send me a product image to get started."
                );
            }

            return new ConversationResponse(
                    ConversationState.NEW,
                    "Please send a product image to get started."
            );
        }

        if (currentState == ConversationState.IMAGE_RECEIVED) {

            context.setCurrentState(ConversationState.PROCESSING);

            return new ConversationResponse(
                    ConversationState.PROCESSING,
                    "Processing your product image..."
            );
        }

        if (currentState == ConversationState.PROCESSING) {

            return new ConversationResponse(
                    ConversationState.PROCESSING,
                    "Your listing is still being processed. Please wait."
            );
        }

        if (currentState == ConversationState.LISTING_READY) {

            context.setCurrentState(ConversationState.AWAITING_APPROVAL);

            return new ConversationResponse(
                    ConversationState.AWAITING_APPROVAL,
                    "Your listing is ready. Approve this listing?"
            );
        }

        if (currentState == ConversationState.AWAITING_APPROVAL) {

            if (intent == IntentType.APPROVE) {

                Long listingId = context.getCurrentListingId();

                if (listingId == null) {
                    context.setCurrentState(ConversationState.FAILED);

                    return new ConversationResponse(
                            ConversationState.FAILED,
                            "Unable to approve the listing because no listing is associated with this conversation."
                    );
                }

                listingService.approveListing(listingId);

                context.setCurrentState(
                        ConversationState.AWAITING_MARKETPLACE
                );

                return new ConversationResponse(
                        ConversationState.AWAITING_MARKETPLACE,
                        "Listing approved. Where would you like to publish?"
                );
            }

            if (intent == IntentType.REJECT) {

                context.setCurrentState(
                        ConversationState.LISTING_READY
                );

                return new ConversationResponse(
                        ConversationState.LISTING_READY,
                        "Listing rejected. We can edit it before publishing."
                );
            }

            return new ConversationResponse(
                    ConversationState.AWAITING_APPROVAL,
                    "Please approve or reject the listing."
            );
        }
        if (currentState == ConversationState.AWAITING_MARKETPLACE) {

            if (intent == IntentType.SELECT_MARKETPLACE) {

                String content = message.getContent();

                if (content == null) {
                    return new ConversationResponse(
                            ConversationState.AWAITING_MARKETPLACE,
                            "Please select a marketplace using 1, 2, 3, or 4."
                    );
                }

                content = content.trim();

                if (content.equals("1")) {

                    context.setSelectedMarketplaces(
                            List.of("EBAY")
                    );

                } else if (content.equals("2")) {

                    context.setSelectedMarketplaces(
                            List.of("VINTED")
                    );

                } else if (content.equals("3")) {

                    context.setSelectedMarketplaces(
                            List.of("DEPOP")
                    );

                } else if (content.equals("4")) {

                    context.setSelectedMarketplaces(
                            List.of(
                                    "EBAY",
                                    "VINTED",
                                    "DEPOP"
                            )
                    );

                } else {

                    return new ConversationResponse(
                            ConversationState.AWAITING_MARKETPLACE,
                            "Please select a marketplace using 1, 2, 3, or 4."
                    );
                }

                context.setCurrentState(
                        ConversationState.PUBLISHING
                );

                return new ConversationResponse(
                        ConversationState.PUBLISHING,
                        "Marketplace selected. Ready to publish."
                );
            }

            return new ConversationResponse(
                    ConversationState.AWAITING_MARKETPLACE,
                    "Please select a marketplace using 1, 2, 3, or 4."
            );
        }

        if (currentState == ConversationState.PUBLISHING) {

            return new ConversationResponse(
                    ConversationState.PUBLISHING,
                    "Listing is ready to be published."
            );
        }

        if (currentState == ConversationState.PUBLISHED) {

            return new ConversationResponse(
                    ConversationState.PUBLISHED,
                    "Your listing has already been published."
            );
        }

        if (currentState == ConversationState.FAILED) {

            return new ConversationResponse(
                    ConversationState.FAILED,
                    "Something went wrong. Please try again."
            );
        }

        return new ConversationResponse(
                currentState,
                "I didn't understand that. Please try again."
        );

    }


}
