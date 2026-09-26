package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.integration.marketplace.MockDepopClient;
import com.aistetic.conversationallayer.integration.marketplace.MockEbayClient;
import com.aistetic.conversationallayer.integration.marketplace.MockVintedClient;
import org.springframework.stereotype.Service;

import com.aistetic.conversationallayer.domain.Listing;
import com.aistetic.conversationallayer.domain.Marketplace;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResult;

import java.util.List;
import java.util.Map;

@Service
public class PublishingService {

    private final MockEbayClient ebayClient;
    private final MockVintedClient vintedClient;
    private final MockDepopClient depopClient;

    public PublishingService(
            MockEbayClient ebayClient,
            MockVintedClient vintedClient,
            MockDepopClient depopClient
    ) {
        this.ebayClient = ebayClient;
        this.vintedClient = vintedClient;
        this.depopClient = depopClient;
    }

    public Map<Marketplace, MarketplacePublicationResult> publishListing(
            Listing listing,
            List<Marketplace> marketplaces
    ) {

        Map<Marketplace, MarketplacePublicationResult> results =
                new java.util.EnumMap<>(Marketplace.class);

        for (Marketplace marketplace : marketplaces) {

            MarketplacePublicationResult result;

            switch (marketplace) {

                case EBAY:
                    result = ebayClient.publishListing(listing);
                    break;

                case VINTED:
                    result = vintedClient.publishListing(listing);
                    break;

                case DEPOP:
                    result = depopClient.publishListing(listing);
                    break;

                default:
                    continue;
            }

            results.put(marketplace, result);
        }

        return results;
    }
}