package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.dto.ProductAnalysis;
import com.aistetic.conversationallayer.exception.AIServiceException;
import com.aistetic.conversationallayer.integration.ai.AIClient;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductAnalysisService {

    private static final Logger logger =
            LoggerFactory.getLogger(ProductAnalysisService.class);

    private final AIClient aiClient;

    public ProductAnalysisService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public ProductAnalysis analyzeProduct(String imageUrl) {

        logger.info("Starting product image analysis");

        try {

            ProductAnalysis result =
                    aiClient.analyzeImage(imageUrl);

            logger.info("Product image analysis completed");

            return result;

        } catch (AIServiceException e) {

            logger.error(
                    "AI service failed during product analysis",
                    e
            );

            throw e;

        } catch (Exception e) {

            logger.error(
                    "Unexpected error during product analysis",
                    e
            );

            throw new AIServiceException(
                    "AI service failed while analyzing the product",
                    e
            );
        }
    }
}