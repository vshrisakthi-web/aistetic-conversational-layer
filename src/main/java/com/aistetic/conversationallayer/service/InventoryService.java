package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.domain.Listing;
import com.aistetic.conversationallayer.domain.MarketplacePublication;
import com.aistetic.conversationallayer.dto.InventoryItemResponse;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResponse;
import com.aistetic.conversationallayer.repository.ListingRepository;
import org.springframework.stereotype.Service;

import com.aistetic.conversationallayer.domain.PublicationStatus;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final ListingRepository listingRepository;

    public InventoryService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<InventoryItemResponse> getInventory() {

        List<Listing> listings = listingRepository.findAll();

        return listings.stream()
                .map(this::convertToResponse)
                .toList();
    }

    private InventoryItemResponse convertToResponse(Listing listing) {

        List<MarketplacePublicationResponse> marketplaces =
                listing.getMarketplacePublications()
                        .stream()
                        .map(this::convertMarketplacePublication)
                        .toList();

        return new InventoryItemResponse(
                listing.getId(),
                listing.getTitle(),
                listing.getPrice(),
                listing.getStatus(),
                marketplaces
        );
    }

    private MarketplacePublicationResponse convertMarketplacePublication(
            MarketplacePublication publication) {

        return new MarketplacePublicationResponse(
                publication.getMarketplace(),
                publication.getStatus(),
                publication.getExternalListingId()
        );
    }
    @Transactional
    public InventoryItemResponse markListingAsSold(Long listingId) {

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() ->
                        new RuntimeException("Listing not found: " + listingId)
                );

        listing.setStatus("SOLD");

        if (listing.getMarketplacePublications() != null) {

            for (MarketplacePublication publication :
                    listing.getMarketplacePublications()) {

                publication.setStatus(
                        PublicationStatus.REMOVED.name()
                );
            }
        }

        listingRepository.save(listing);

        return convertToResponse(listing);
    }
}