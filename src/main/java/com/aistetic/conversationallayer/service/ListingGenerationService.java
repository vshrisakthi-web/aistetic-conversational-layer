package com.aistetic.conversationallayer.service;
import com.aistetic.conversationallayer.dto.ListingDraft;
import com.aistetic.conversationallayer.dto.ProductAnalysis;
import com.aistetic.conversationallayer.integration.ai.AIClient;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ListingGenerationService {

    private static final Logger logger =
            LoggerFactory.getLogger(ListingGenerationService.class);

    private final AIClient aiClient;

    public ListingGenerationService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public ListingDraft generateListing(ProductAnalysis productAnalysis) {

        logger.info("Starting listing generation");

        ListingDraft listingDraft =
                aiClient.generateListing(productAnalysis);

        logger.info(
                "Listing generation completed. listingId={}",
                listingDraft.getListingId()
        );

        return listingDraft;
    }
}
