package com.aistetic.conversationallayer.controller;

import com.aistetic.conversationallayer.dto.InventoryItemResponse;
import com.aistetic.conversationallayer.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryItemResponse> getInventory() {

        return inventoryService.getInventory();
    }
    @PostMapping("/{listingId}/sold")
    public InventoryItemResponse markListingAsSold(
            @PathVariable Long listingId) {

        return inventoryService.markListingAsSold(listingId);
    }
}