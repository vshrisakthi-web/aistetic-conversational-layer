package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.dto.ProductAnalysis;
import com.aistetic.conversationallayer.integration.ai.AIClient;
import org.springframework.stereotype.Service;


@Service
public class ProductAnalysisService {
    private final AIClient aiClient;

    public ProductAnalysisService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public ProductAnalysis analyzeProduct(String imageUrl) {
        return aiClient.analyzeImage(imageUrl);
    }
}
