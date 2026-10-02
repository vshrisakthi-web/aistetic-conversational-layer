package com.aistetic.conversationallayer.dto;

import java.math.BigDecimal;
import java.util.List;

public class ListingDetailsResponse {

    private Long id;
    private String title;
    private String description;
    private String brand;
    private String category;
    private String color;
    private String size;
    private String condition;
    private BigDecimal price;
    private String status;
    private String imageUrl;
    private List<MarketplacePublicationResponse> marketplacePublications;

    public ListingDetailsResponse() {
    }

    public ListingDetailsResponse(
            Long id,
            String title,
            String description,
            String brand,
            String category,
            String color,
            String size,
            String condition,
            BigDecimal price,
            String status,
            String imageUrl,
            List<MarketplacePublicationResponse> marketplacePublications) {

        this.id = id;
        this.title = title;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.color = color;
        this.size = size;
        this.condition = condition;
        this.price = price;
        this.status = status;
        this.imageUrl = imageUrl;
        this.marketplacePublications = marketplacePublications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<MarketplacePublicationResponse> getMarketplacePublications() {
        return marketplacePublications;
    }

    public void setMarketplacePublications(
            List<MarketplacePublicationResponse> marketplacePublications) {
        this.marketplacePublications = marketplacePublications;
    }
}