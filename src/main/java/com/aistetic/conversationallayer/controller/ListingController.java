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

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/listings")
public class ListingController {

    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @PostMapping("/analyze")
    public ListingDraft analyzeListing() {
        return listingService.generateListing("mock-image-url");
    }

    @GetMapping("/{listingId}")
    public ListingDetailsResponse getListingById(
            @PathVariable Long listingId) {

        return listingService.getListingDetailsById(listingId);
    }

   }