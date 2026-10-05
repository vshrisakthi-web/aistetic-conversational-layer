package com.aistetic.conversationallayer.orchestrator;

import com.aistetic.conversationallayer.domain.ConversationState;
import com.aistetic.conversationallayer.domain.IntentType;
import com.aistetic.conversationallayer.domain.Message;
import com.aistetic.conversationallayer.domain.Listing;
import com.aistetic.conversationallayer.domain.Marketplace;
import com.aistetic.conversationallayer.dto.ConversationResponse;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResult;
import com.aistetic.conversationallayer.dto.ListingDraft;
import com.aistetic.conversationallayer.service.ConversationContext;
import com.aistetic.conversationallayer.service.IntentDetectionService;
import com.aistetic.conversationallayer.service.ListingService;
import com.aistetic.conversationallayer.service.PublishingService;
import org.springframework.stereotype.Service;
import com.aistetic.conversationallayer.exception.AIServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

@Service
public class ConversationOrchestrator {

    private static final Logger logger =
            LoggerFactory.getLogger(ConversationOrchestrator.class);

    private final IntentDetectionService intentDetectionService;
    private final ListingService listingService;
    private final PublishingService publishingService;

    public ConversationOrchestrator(
            IntentDetectionService intentDetectionService,
            ListingService listingService,
            PublishingService publishingService) {

        this.intentDetectionService = intentDetectionService;
        this.listingService = listingService;
        this.publishingService = publishingService;
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

        logger.info(
                "Processing conversation. state={}, intent={}",
                currentState,
                intent
        );

        // ============================================================
        // NEW
        // ============================================================

        if (currentState == ConversationState.NEW) {

            if (intent == IntentType.UPLOAD_IMAGE) {

                context.setCurrentState(
                        ConversationState.IMAGE_RECEIVED
                );

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

        // ============================================================
        // IMAGE RECEIVED
        // ============================================================

        if (currentState == ConversationState.IMAGE_RECEIVED) {

            context.setCurrentState(
                    ConversationState.PROCESSING
            );

            try {

                String imageUrl = message.getContent();

                ListingDraft listingDraft =
                        listingService.generateListing(imageUrl);

                logger.info(
                        "Listing generation completed. listingId={}",
                        listingDraft.getListingId()
                );

                if (listingDraft.getListingId() == null) {

                    context.setCurrentState(
                            ConversationState.FAILED
                    );

                    return new ConversationResponse(
                            ConversationState.FAILED,
                            "Unable to create the listing."
                    );
                }

                context.setCurrentListingId(
                        listingDraft.getListingId()
                );

                context.setCurrentState(
                        ConversationState.AWAITING_APPROVAL
                );

                String approvalMessage =
                        "Your listing is ready.\n\n" +
                                "Title: " + listingDraft.getTitle() + "\n" +
                                "Color: " + listingDraft.getColor() + "\n" +
                                "Size: " + listingDraft.getSize() + "\n" +
                                "Price: ₹" + listingDraft.getPrice() + "\n" +
                                "Condition: " + listingDraft.getCondition() + "\n\n" +
                                "Approve this listing?\n\n" +
                                "Reply with Yes or No.";

                return new ConversationResponse(
                        ConversationState.AWAITING_APPROVAL,
                        approvalMessage
                );

            } catch (AIServiceException e) {

                context.setCurrentState(
                        ConversationState.FAILED
                );

                return new ConversationResponse(
                        ConversationState.FAILED,
                        "I couldn't analyze the product right now. Please try again."
                );

            } catch (Exception e) {

                context.setCurrentState(
                        ConversationState.FAILED
                );

                return new ConversationResponse(
                        ConversationState.FAILED,
                        "Unable to process the product image."
                );
            }

        }

        // ============================================================
        // PROCESSING
        // ============================================================

        if (currentState == ConversationState.PROCESSING) {

            return new ConversationResponse(
                    ConversationState.PROCESSING,
                    "Your listing is still being processed. Please wait."
            );
        }

        // ============================================================
        // LISTING READY
        // ============================================================

        if (currentState == ConversationState.LISTING_READY) {

            context.setCurrentState(
                    ConversationState.AWAITING_APPROVAL
            );

            return new ConversationResponse(
                    ConversationState.AWAITING_APPROVAL,
                    "Your listing is ready. Approve this listing?"
            );
        }

        // ============================================================
        // AWAITING APPROVAL
        // ============================================================

        if (currentState == ConversationState.AWAITING_APPROVAL) {

            if (intent == IntentType.APPROVE) {

                Long listingId = context.getCurrentListingId();

                if (listingId == null) {

                    context.setCurrentState(
                            ConversationState.FAILED
                    );

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
                        "Listing approved.\n\n" +
                                "Where would you like to publish?\n\n" +
                                "1 - eBay\n" +
                                "2 - Vinted\n" +
                                "3 - Depop\n" +
                                "4 - All marketplaces\n\n" +
                                "Reply with 1, 2, 3, or 4."
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

        // ============================================================
        // AWAITING MARKETPLACE
        // ============================================================

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

                // 1 = eBay
                if (content.equals("1")) {

                    context.setSelectedMarketplaces(
                            List.of("EBAY")
                    );

                    // 2 = Vinted
                } else if (content.equals("2")) {

                    context.setSelectedMarketplaces(
                            List.of("VINTED")
                    );

                    // 3 = Depop
                } else if (content.equals("3")) {

                    context.setSelectedMarketplaces(
                            List.of("DEPOP")
                    );

                    // 4 = All marketplaces
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
                        "Marketplace selected.\n\n" +
                                "Ready to publish?\n\n" +
                                "Reply with Publish or Cancel."
                );
            }

            return new ConversationResponse(
                    ConversationState.AWAITING_MARKETPLACE,
                    "Please select a marketplace using 1, 2, 3, or 4."
            );
        }

        // ============================================================
        // PUBLISHING
        // TASK 58
        // ============================================================

        if (currentState == ConversationState.PUBLISHING) {

            Long listingId = context.getCurrentListingId();

            // --------------------------------------------------------
            // Check whether a listing is associated with conversation
            // --------------------------------------------------------

            if (listingId == null) {

                context.setCurrentState(
                        ConversationState.FAILED
                );

                return new ConversationResponse(
                        ConversationState.FAILED,
                        "Unable to publish because no listing is associated with this conversation."
                );
            }

            // --------------------------------------------------------
            // Get the actual listing
            // --------------------------------------------------------

            Listing listing;

            try {

                listing = listingService.getListingById(listingId);

            } catch (RuntimeException e) {

                context.setCurrentState(
                        ConversationState.FAILED
                );

                return new ConversationResponse(
                        ConversationState.FAILED,
                        "Unable to publish because the listing could not be found."
                );
            }

            // --------------------------------------------------------
            // Convert String marketplaces to Marketplace enum
            // --------------------------------------------------------

            List<Marketplace> marketplaces =
                    context.getSelectedMarketplaces()
                            .stream()
                            .map(Marketplace::valueOf)
                            .toList();

            // --------------------------------------------------------
            // Publish the listing
            // --------------------------------------------------------

            Map<Marketplace, MarketplacePublicationResult> results =
                    publishingService.publishListing(
                            listing,
                            marketplaces
                    );

            logger.info(
                    "Publishing completed. listingId={}, marketplaces={}",
                    listingId,
                    marketplaces
            );

            // --------------------------------------------------------
            // Check whether all marketplaces succeeded
            // --------------------------------------------------------

            boolean allSuccessful = results.values()
                    .stream()
                    .allMatch(MarketplacePublicationResult::success);

            // --------------------------------------------------------
            // Publishing successful
            // --------------------------------------------------------

            if (allSuccessful) {

                logger.info(
                        "Listing published successfully. listingId={}, marketplaces={}",
                        listingId,
                        marketplaces
                );

                context.setCurrentState(
                        ConversationState.PUBLISHED
                );

                return new ConversationResponse(
                        ConversationState.PUBLISHED,
                        "All selected marketplaces published successfully."
                );
            }

            // --------------------------------------------------------
            // Publishing failed
            // --------------------------------------------------------

            context.setCurrentState(
                    ConversationState.FAILED
            );

            logger.error(
                    "Listing publishing failed. listingId={}, marketplaces={}",
                    listingId,
                    marketplaces
            );

            return new ConversationResponse(
                    ConversationState.FAILED,
                    "Some marketplace publications failed."
            );
        }

        // ============================================================
        // PUBLISHED
        // ============================================================

        if (currentState == ConversationState.PUBLISHED) {

            if (intent == IntentType.UPLOAD_IMAGE) {

                context.setCurrentState(
                        ConversationState.IMAGE_RECEIVED
                );

                return new ConversationResponse(
                        ConversationState.IMAGE_RECEIVED,
                        "Image received."
                );
            }

            return new ConversationResponse(
                    ConversationState.PUBLISHED,
                    "Your listing has already been published."
            );
        }

        // ============================================================
        // FAILED
        // ============================================================

        if (currentState == ConversationState.FAILED) {

            return new ConversationResponse(
                    ConversationState.FAILED,
                    "Something went wrong. Please try again."
            );
        }

        // ============================================================
        // DEFAULT
        // ============================================================

        return new ConversationResponse(
                currentState,
                "I didn't understand that. Please try again."
        );
    }
}