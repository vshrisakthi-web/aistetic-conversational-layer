package com.aistetic.conversationallayer.integration.ai;
import com.aistetic.conversationallayer.dto.ListingDraft;
import com.aistetic.conversationallayer.dto.ProductAnalysis;
public interface AIClient {
    ProductAnalysis analyzeImage(String imageUrl);
    ListingDraft generateListing(ProductAnalysis productAnalysis);
}
