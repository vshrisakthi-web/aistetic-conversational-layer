package com.aistetic.conversationallayer.integration.ai;
import com.aistetic.conversationallayer.dto.ListingDraft;
import com.aistetic.conversationallayer.dto.ProductAnalysis;

import org.springframework.stereotype.Component;

@Component
public class MockAIClient implements AIClient {
    @Override
    public ProductAnalysis analyzeImage(String imageUrl) {

        ProductAnalysis productAnalysis = new ProductAnalysis();

        productAnalysis.setBrand("Nike");
        productAnalysis.setCategory("Footwear");
        productAnalysis.setProductType("Sneakers");
        productAnalysis.setColor("White");
        productAnalysis.setSize("UK 9");
        productAnalysis.setMaterial("Leather");
        productAnalysis.setCondition("Good");

        return productAnalysis;
    }

    @Override
    public ListingDraft generateListing(ProductAnalysis productAnalysis) {

        ListingDraft listingDraft = new ListingDraft();

        listingDraft.setTitle(
                productAnalysis.getBrand()
                        + " "
                        + productAnalysis.getProductType()
                        + " "
                        + productAnalysis.getColor()
                        + " "
                        + productAnalysis.getSize()
        );

        listingDraft.setDescription(
                productAnalysis.getBrand()
                        + " "
                        + productAnalysis.getProductType()
                        + " in "
                        + productAnalysis.getColor()
                        + ". Condition: "
                        + productAnalysis.getCondition()
                        + "."
        );

        listingDraft.setBrand(productAnalysis.getBrand());
        listingDraft.setCategory(productAnalysis.getCategory());
        listingDraft.setColor(productAnalysis.getColor());
        listingDraft.setSize(productAnalysis.getSize());
        listingDraft.setCondition(productAnalysis.getCondition());

        return listingDraft;
    }
}