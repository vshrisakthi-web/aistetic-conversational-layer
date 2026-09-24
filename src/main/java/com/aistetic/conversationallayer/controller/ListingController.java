package com.aistetic.conversationallayer.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.aistetic.conversationallayer.service.ListingService;
import com.aistetic.conversationallayer.dto.ListingDraft;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
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

}