package com.aistetic.conversationallayer.dto;

import java.math.BigDecimal;
import java.util.List;

public class InventoryItemResponse {

    private Long listingId;
    private String title;
    private BigDecimal price;
    private String status;
    private List<MarketplacePublicationResponse> marketplaces;

    public InventoryItemResponse() {
    }

    public InventoryItemResponse(
            Long listingId,
            String title,
            BigDecimal price,
            String status,
            List<MarketplacePublicationResponse> marketplaces) {

        this.listingId = listingId;
        this.title = title;
        this.price = price;
        this.status = status;
        this.marketplaces = marketplaces;
    }

    public Long getListingId() {
        return listingId;
    }

    public void setListingId(Long listingId) {
        this.listingId = listingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<MarketplacePublicationResponse> getMarketplaces() {
        return marketplaces;
    }

    public void setMarketplaces(
            List<MarketplacePublicationResponse> marketplaces) {

        this.marketplaces = marketplaces;
    }
}