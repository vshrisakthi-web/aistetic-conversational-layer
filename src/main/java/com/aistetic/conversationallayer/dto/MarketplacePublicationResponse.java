package com.aistetic.conversationallayer.dto;

public class MarketplacePublicationResponse {

    private String marketplace;
    private String status;
    private String externalListingId;

    public MarketplacePublicationResponse() {
    }

    public MarketplacePublicationResponse(
            String marketplace,
            String status,
            String externalListingId) {

        this.marketplace = marketplace;
        this.status = status;
        this.externalListingId = externalListingId;
    }

    public String getMarketplace() {
        return marketplace;
    }

    public void setMarketplace(String marketplace) {
        this.marketplace = marketplace;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExternalListingId() {
        return externalListingId;
    }

    public void setExternalListingId(String externalListingId) {
        this.externalListingId = externalListingId;
    }
}
