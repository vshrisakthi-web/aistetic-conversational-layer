package com.aistetic.conversationallayer.integration.marketplace;

import com.aistetic.conversationallayer.domain.Listing;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResult;
import org.springframework.stereotype.Component;
@Component
public class MockDepopClient implements MarketplaceClient {

    @Override
    public MarketplacePublicationResult publishListing(Listing listing) {

        return new MarketplacePublicationResult(
                true,
                "DEPOP-30001"
        );
    }

    @Override
    public void updateListing(Listing listing) {
        // Mock implementation
    }

    @Override
    public void removeListing(Listing listing) {
        // Mock implementation
    }

    @Override
    public void getListingStatus(Listing listing) {
        // Mock implementation
    }
}