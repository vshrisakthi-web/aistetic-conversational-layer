package com.aistetic.conversationallayer.controller;

import com.aistetic.conversationallayer.dto.ListingDraft;
import com.aistetic.conversationallayer.dto.ListingDetailsResponse;
import com.aistetic.conversationallayer.service.ListingService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
        name = "Listings",
        description = "Listing creation and retrieval endpoints"
)
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/listings")

public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @Operation(
            summary = "Generate listing",
            description = "Generates a listing draft from the current mock image input."
    )
    @PostMapping("/analyze")

    public ListingDraft analyzeListing() {

        return listingService.generateListing("mock-image-url");
    }

    @Operation(
            summary = "Get listing by ID",
            description = "Returns detailed information about a listing."
    )
    @GetMapping("/{listingId}")

    public ListingDetailsResponse getListingById(
            @PathVariable Long listingId) {

        return listingService.getListingDetailsById(listingId);
    }

   }