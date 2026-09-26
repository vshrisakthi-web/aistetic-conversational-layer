package com.aistetic.conversationallayer.integration.marketplace;

import com.aistetic.conversationallayer.domain.Listing;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResult;

public interface MarketplaceClient {

    MarketplacePublicationResult publishListing(Listing listing);

    void updateListing(Listing listing);

    void removeListing(Listing listing);

    void getListingStatus(Listing listing);
}