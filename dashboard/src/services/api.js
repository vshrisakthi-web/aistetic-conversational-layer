const API_BASE_URL = "http://localhost:8080";

export async function getInventory() {
    const response = await fetch(`${API_BASE_URL}/api/inventory`);

    if (!response.ok) {
        throw new Error(`Failed to fetch inventory: ${response.status}`);
    }

    return response.json();
}

export async function getListingById(listingId) {
    const response = await fetch(
        `${API_BASE_URL}/api/listings/${listingId}`
    );

    if (!response.ok) {
        throw new Error(
            `Failed to fetch listing: ${response.status}`
        );
    }

    return response.json();
}
export async function getDashboardKpis() {

    const response = await fetch(
        `${API_BASE_URL}/api/inventory/kpis`
    );

    if (!response.ok) {
        throw new Error(
            `Failed to fetch dashboard KPIs: ${response.status}`
        );
    }

    return response.json();
}
export async function getConversationActivity(conversationId) {
    const response = await fetch(
        `${API_BASE_URL}/api/conversations/${conversationId}/messages`
    );

    if (!response.ok) {
        throw new Error(
            `Failed to fetch conversation activity: ${response.status}`
        );
    }

    return response.json();
}