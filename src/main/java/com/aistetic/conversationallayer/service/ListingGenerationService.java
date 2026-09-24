package com.aistetic.conversationallayer.service;
import com.aistetic.conversationallayer.dto.ListingDraft;
import com.aistetic.conversationallayer.dto.ProductAnalysis;
import com.aistetic.conversationallayer.integration.ai.AIClient;
import org.springframework.stereotype.Service;

@Service
public class ListingGenerationService {

    private final AIClient aiClient;

    public ListingGenerationService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public ListingDraft generateListing(ProductAnalysis productAnalysis){
        return aiClient.generateListing(productAnalysis);
    }
}
