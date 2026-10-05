package com.aistetic.conversationallayer.service;

import com.aistetic.conversationallayer.domain.Listing;
import com.aistetic.conversationallayer.domain.Marketplace;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResult;
import com.aistetic.conversationallayer.integration.marketplace.MockDepopClient;
import com.aistetic.conversationallayer.integration.marketplace.MockEbayClient;
import com.aistetic.conversationallayer.integration.marketplace.MockVintedClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.aistetic.conversationallayer.repository.MarketplacePublicationRepository;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PublishingServiceTest {

    @Mock
    private MockEbayClient ebayClient;

    @Mock
    private MockVintedClient vintedClient;

    @Mock
    private MockDepopClient depopClient;
    @Mock
    private MarketplacePublicationRepository marketplacePublicationRepository;

    @InjectMocks
    private PublishingService publishingService;

    @Test
    void shouldPublishListingToAllSelectedMarketplaces() {

        Listing listing = org.mockito.Mockito.mock(Listing.class);

        MarketplacePublicationResult ebayResult =
                new MarketplacePublicationResult(true, "EBAY-10001");

        MarketplacePublicationResult vintedResult =
                new MarketplacePublicationResult(true, "VINTED-20001");

        MarketplacePublicationResult depopResult =
                new MarketplacePublicationResult(true, "DEPOP-30001");

        when(ebayClient.publishListing(listing))
                .thenReturn(ebayResult);

        when(vintedClient.publishListing(listing))
                .thenReturn(vintedResult);

        when(depopClient.publishListing(listing))
                .thenReturn(depopResult);

        List<Marketplace> marketplaces = List.of(
                Marketplace.EBAY,
                Marketplace.VINTED,
                Marketplace.DEPOP
        );

        Map<Marketplace, MarketplacePublicationResult> results =
                publishingService.publishListing(listing, marketplaces);

        assertNotNull(results);

        assertEquals(3, results.size());

        assertEquals(ebayResult, results.get(Marketplace.EBAY));
        assertEquals(vintedResult, results.get(Marketplace.VINTED));
        assertEquals(depopResult, results.get(Marketplace.DEPOP));

        verify(ebayClient).publishListing(listing);
        verify(vintedClient).publishListing(listing);
        verify(depopClient).publishListing(listing);
    }
    @Test
    void shouldHandleMarketplaceFailure() {

        Listing listing = org.mockito.Mockito.mock(Listing.class);

        MarketplacePublicationResult ebayFailure =
                new MarketplacePublicationResult(false, null);

        MarketplacePublicationResult vintedSuccess =
                new MarketplacePublicationResult(true, "VINTED-20001");

        when(ebayClient.publishListing(listing))
                .thenReturn(ebayFailure);

        when(vintedClient.publishListing(listing))
                .thenReturn(vintedSuccess);

        List<Marketplace> marketplaces = List.of(
                Marketplace.EBAY,
                Marketplace.VINTED
        );

        Map<Marketplace, MarketplacePublicationResult> results =
                publishingService.publishListing(listing, marketplaces);

        assertNotNull(results);

        assertEquals(2, results.size());

        assertEquals(
                ebayFailure,
                results.get(Marketplace.EBAY)
        );

        assertEquals(
                vintedSuccess,
                results.get(Marketplace.VINTED)
        );

        verify(ebayClient, times(3))
                .publishListing(listing);

        verify(vintedClient, times(1))
                .publishListing(listing);
    }
    @Test
    void shouldRetryMarketplacePublishingUntilSuccess() {

        Listing listing = org.mockito.Mockito.mock(Listing.class);

        MarketplacePublicationResult failure =
                new MarketplacePublicationResult(false, null);

        MarketplacePublicationResult success =
                new MarketplacePublicationResult(true, "EBAY-10001");

        when(ebayClient.publishListing(listing))
                .thenReturn(
                        failure,
                        failure,
                        success
                );

        List<Marketplace> marketplaces =
                List.of(Marketplace.EBAY);

        Map<Marketplace, MarketplacePublicationResult> results =
                publishingService.publishListing(
                        listing,
                        marketplaces
                );

        assertNotNull(results);

        assertEquals(1, results.size());

        assertEquals(
                success,
                results.get(Marketplace.EBAY)
        );

        verify(
                ebayClient,
                times(3)
        ).publishListing(listing);
    }
    @Test
    void shouldStopAfterMaximumRetries() {

        Listing listing = org.mockito.Mockito.mock(Listing.class);

        MarketplacePublicationResult failure =
                new MarketplacePublicationResult(false, null);

        when(ebayClient.publishListing(listing))
                .thenReturn(
                        failure,
                        failure,
                        failure
                );

        List<Marketplace> marketplaces =
                List.of(Marketplace.EBAY);

        Map<Marketplace, MarketplacePublicationResult> results =
                publishingService.publishListing(
                        listing,
                        marketplaces
                );

        assertNotNull(results);

        assertEquals(1, results.size());

        assertEquals(
                failure,
                results.get(Marketplace.EBAY)
        );

        verify(
                ebayClient,
                times(3)
        ).publishListing(listing);
    }
}