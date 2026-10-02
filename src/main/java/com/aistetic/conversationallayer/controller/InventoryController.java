package com.aistetic.conversationallayer.controller;

import com.aistetic.conversationallayer.dto.InventoryItemResponse;
import com.aistetic.conversationallayer.service.InventoryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import com.aistetic.conversationallayer.dto.DashboardKpiResponse;
import com.aistetic.conversationallayer.service.ListingService;



@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ListingService listingService;

    public InventoryController(
            InventoryService inventoryService,
            ListingService listingService) {

        this.inventoryService = inventoryService;
        this.listingService = listingService;
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
    @GetMapping("/kpis")
    public DashboardKpiResponse getDashboardKpis() {

        return listingService.getDashboardKpis();
    }
}