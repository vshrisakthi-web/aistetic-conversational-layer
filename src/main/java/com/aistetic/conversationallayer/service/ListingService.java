package com.aistetic.conversationallayer.service;
import com.aistetic.conversationallayer.dto.ListingDraft;
import com.aistetic.conversationallayer.dto.ProductAnalysis;
import org.springframework.stereotype.Service;
import com.aistetic.conversationallayer.repository.ListingRepository;
import com.aistetic.conversationallayer.domain.Listing;
import java.math.BigDecimal;
import com.aistetic.conversationallayer.repository.UserRepository;
import com.aistetic.conversationallayer.domain.User;

import com.aistetic.conversationallayer.dto.ListingDetailsResponse;
import com.aistetic.conversationallayer.dto.MarketplacePublicationResponse;
import com.aistetic.conversationallayer.domain.MarketplacePublication;
import java.util.List;
import java.util.stream.Collectors;
import com.aistetic.conversationallayer.dto.DashboardKpiResponse;


@Service
public class ListingService {

    private final ProductAnalysisService productAnalysisService;
    private final ListingGenerationService listingGenerationService;
    private final PricingService pricingService;
    private final ListingRepository listingRepository;
    private final UserRepository userRepository;

    //constructor injection
    public ListingService(
            ProductAnalysisService productAnalysisService,
            ListingGenerationService listingGenerationService,
            PricingService pricingService,
            ListingRepository listingRepository, UserRepository userRepository) {

        this.productAnalysisService = productAnalysisService;
        this.listingGenerationService = listingGenerationService;
        this.pricingService = pricingService;
        this.listingRepository = listingRepository;
        this.userRepository = userRepository;
    }

    public ListingDraft generateListing(String imageUrl) {


        ProductAnalysis productAnalysis =
                productAnalysisService.analyzeProduct(imageUrl);
        ListingDraft listingDraft =
                listingGenerationService.generateListing(productAnalysis);
        Integer suggestedPrice =
                pricingService.suggestPrice(productAnalysis);
        listingDraft.setPrice(suggestedPrice);

        // Convert ListingDraft → Listing
        Listing listing = new Listing();
        User user = userRepository.findById(1L)
                .orElseThrow();
        listing.setUser(user);

        listing.setTitle(listingDraft.getTitle());
        listing.setDescription(listingDraft.getDescription());
        listing.setBrand(listingDraft.getBrand());
        listing.setCategory(listingDraft.getCategory());
        listing.setColor(listingDraft.getColor());
        listing.setSize(listingDraft.getSize());
        listing.setCondition(listingDraft.getCondition());

        listing.setPrice(
                BigDecimal.valueOf(listingDraft.getPrice())
        );

        listing.setStatus("DRAFT");

        listing = listingRepository.save(listing);

        listingDraft.setListingId(listing.getId());

        return listingDraft;
    }

    public Listing approveListing(Long listingId) {

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() ->
                        new RuntimeException("Listing not found: " + listingId)
                );

        listing.setStatus("APPROVED");

        return listingRepository.save(listing);
    }

    public Listing getListingById(Long listingId) {

        return listingRepository.findById(listingId)
                .orElseThrow(() ->
                        new RuntimeException("Listing not found: " + listingId)
                );
    }

    public ListingDetailsResponse getListingDetailsById(Long listingId) {

        Listing listing = listingRepository.findById(listingId)
                .orElseThrow(() ->
                        new RuntimeException("Listing not found: " + listingId)
                );

        List<MarketplacePublicationResponse> publications =
                listing.getMarketplacePublications()
                        .stream()
                        .map(publication ->
                                new MarketplacePublicationResponse(
                                        publication.getMarketplace(),
                                        publication.getStatus(),
                                        publication.getExternalListingId()
                                )
                        )
                        .collect(Collectors.toList());

        return new ListingDetailsResponse(
                listing.getId(),
                listing.getTitle(),
                listing.getDescription(),
                listing.getBrand(),
                listing.getCategory(),
                listing.getColor(),
                listing.getSize(),
                listing.getCondition(),
                listing.getPrice(),
                listing.getStatus(),
                listing.getImageUrl(),
                publications
        );
    }
    public DashboardKpiResponse getDashboardKpis() {

        long totalListings =
                listingRepository.count();

        long draftListings =
                listingRepository.countByStatus("DRAFT");

        long approvedListings =
                listingRepository.countByStatus("APPROVED");

        long publishedListings =
                listingRepository.countByStatus("PUBLISHED");

        return new DashboardKpiResponse(
                totalListings,
                draftListings,
                approvedListings,
                publishedListings
        );
    }
}
