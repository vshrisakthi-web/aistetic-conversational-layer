function MarketplaceStatus({ marketplacePublications }) {
    if (!marketplacePublications || marketplacePublications.length === 0) {
        return (
            <div>
                <h3>Marketplace Status</h3>
                <p>No marketplace publications found.</p>
            </div>
        );
    }

    return (
        <div>
            <h3>Marketplace Status</h3>

            {marketplacePublications.map((publication) => (
                <div
                    key={publication.externalListingId}
                    style={{
                        border: "1px solid #ddd",
                        padding: "15px",
                        marginBottom: "10px",
                        borderRadius: "8px"
                    }}
                >
                    <h4>{publication.marketplace}</h4>

                    <p>
                        <strong>Status:</strong>{" "}
                        {publication.status}
                    </p>

                    <p>
                        <strong>External Listing ID:</strong>{" "}
                        {publication.externalListingId}
                    </p>
                </div>
            ))}
        </div>
    );
}

export default MarketplaceStatus;