package com.aistetic.conversationallayer.dto;

public class DashboardKpiResponse {

    private long totalListings;
    private long draftListings;
    private long approvedListings;
    private long publishedListings;

    public DashboardKpiResponse() {
    }

    public DashboardKpiResponse(
            long totalListings,
            long draftListings,
            long approvedListings,
            long publishedListings) {

        this.totalListings = totalListings;
        this.draftListings = draftListings;
        this.approvedListings = approvedListings;
        this.publishedListings = publishedListings;
    }

    public long getTotalListings() {
        return totalListings;
    }

    public void setTotalListings(long totalListings) {
        this.totalListings = totalListings;
    }

    public long getDraftListings() {
        return draftListings;
    }

    public void setDraftListings(long draftListings) {
        this.draftListings = draftListings;
    }

    public long getApprovedListings() {
        return approvedListings;
    }

    public void setApprovedListings(long approvedListings) {
        this.approvedListings = approvedListings;
    }

    public long getPublishedListings() {
        return publishedListings;
    }

    public void setPublishedListings(long publishedListings) {
        this.publishedListings = publishedListings;
    }
}