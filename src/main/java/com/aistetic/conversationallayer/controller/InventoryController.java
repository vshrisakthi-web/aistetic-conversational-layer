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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(
        name = "Inventory",
        description = "Inventory and dashboard endpoints"
)
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

    @Operation(
            summary = "Get inventory",
            description = "Returns the current inventory of listings."
    )
    @GetMapping
    public List<InventoryItemResponse> getInventory() {

        return inventoryService.getInventory();
    }

    @Operation(
            summary = "Mark listing as sold",
            description = "Marks the specified listing as sold."
    )
    @PostMapping("/{listingId}/sold")
    public InventoryItemResponse markListingAsSold(
            @PathVariable Long listingId) {

        return inventoryService.markListingAsSold(listingId);
    }

    @Operation(
            summary = "Get dashboard KPIs",
            description = "Returns dashboard key performance indicators."
    )
    @GetMapping("/kpis")
    public DashboardKpiResponse getDashboardKpis() {

        return listingService.getDashboardKpis();
    }
}