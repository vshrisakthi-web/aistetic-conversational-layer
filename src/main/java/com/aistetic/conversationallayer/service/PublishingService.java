package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.domain.Listing;
import com.aistetic.conversationallayer.domain.Marketplace;
import com.aistetic.conversationallayer.domain.MarketplacePublication;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResult;
import com.aistetic.conversationallayer.integration.marketplace.MockDepopClient;
import com.aistetic.conversationallayer.integration.marketplace.MockEbayClient;
import com.aistetic.conversationallayer.integration.marketplace.MockVintedClient;
import com.aistetic.conversationallayer.repository.MarketplacePublicationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class PublishingService {

    private final MockEbayClient ebayClient;
    private final MockVintedClient vintedClient;
    private final MockDepopClient depopClient;
    private final MarketplacePublicationRepository marketplacePublicationRepository;

    public PublishingService(
            MockEbayClient ebayClient,
            MockVintedClient vintedClient,
            MockDepopClient depopClient,
            MarketplacePublicationRepository marketplacePublicationRepository
    ) {
        this.ebayClient = ebayClient;
        this.vintedClient = vintedClient;
        this.depopClient = depopClient;
        this.marketplacePublicationRepository = marketplacePublicationRepository;
    }

    public Map<Marketplace, MarketplacePublicationResult> publishListing(
            Listing listing,
            List<Marketplace> marketplaces
    ) {

        Map<Marketplace, MarketplacePublicationResult> results =
                new EnumMap<>(Marketplace.class);

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

            MarketplacePublication publication =
                    new MarketplacePublication();

            publication.setListing(listing);
            publication.setMarketplace(marketplace.name());
            publication.setExternalListingId(
                    result.externalListingId()
            );

            if (result.success()) {

                publication.setStatus("PUBLISHED");
                publication.setPublishedAt(
                        LocalDateTime.now()
                );

            } else {

                publication.setStatus("FAILED");
            }

            marketplacePublicationRepository.save(publication);
        }

        return results;
    }
}