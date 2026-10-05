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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class PublishingService {

    private static final Logger logger =
            LoggerFactory.getLogger(PublishingService.class);

    private static final int MAX_RETRIES = 3;

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

            logger.info(
                    "Starting publication. marketplace={}",
                    marketplace
            );

            MarketplacePublicationResult result =
                    publishWithRetry(marketplace, listing);

            results.put(marketplace, result);

            if (result.success()) {
                logger.info(
                        "Publication successful. marketplace={}, externalListingId={}",
                        marketplace,
                        result.externalListingId()
                );
            } else {
                logger.error(
                        "Publication failed. marketplace={}",
                        marketplace
                );
            }

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

    /**
     * Attempts to publish a listing to a marketplace.
     * If publishing fails, it retries up to MAX_RETRIES times.
     */
    private MarketplacePublicationResult publishWithRetry(
            Marketplace marketplace,
            Listing listing
    ) {

        MarketplacePublicationResult result = null;

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {

            logger.info(
                    "Publishing attempt {} of {}. marketplace={}",
                    attempt,
                    MAX_RETRIES,
                    marketplace
            );

            result = switch (marketplace) {

                case EBAY ->
                        ebayClient.publishListing(listing);

                case VINTED ->
                        vintedClient.publishListing(listing);

                case DEPOP ->
                        depopClient.publishListing(listing);
            };

            if (result.success()) {
                return result;
            }
        }

        return result;
    }
}