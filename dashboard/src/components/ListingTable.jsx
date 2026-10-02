function ListingTable({ listings }) {
    if (!listings || listings.length === 0) {
        return <p>No listings found.</p>;
    }

    return (
        <div className="listing-table-container">
            <table className="listing-table">
                <thead>
                <tr>
                    <th>Listing ID</th>
                    <th>Product</th>
                    <th>Price</th>
                    <th>Status</th>
                    <th>Marketplaces</th>
                </tr>
                </thead>

                <tbody>
                {listings.map((listing) => (
                    <tr
                        key={listing.listingId}
                        onClick={() => window.location.href = `/listings/${listing.listingId}`}
                        style={{ cursor: "pointer" }}
                    >
                        <td>{listing.listingId}</td>

                        <td>{listing.title}</td>

                        <td>₹{Number(listing.price).toLocaleString("en-IN")}</td>

                        <td>
                <span className="status">
                  {listing.status}
                </span>
                        </td>

                        <td>
                            {listing.marketplaces && listing.marketplaces.length > 0 ? (
                                <div className="marketplace-list">
                                    {listing.marketplaces.map((marketplace, index) => (
                                        <span
                                            className="marketplace"
                                            key={`${marketplace.marketplace}-${index}`}
                                        >
                        {marketplace.marketplace}
                      </span>
                                    ))}
                                </div>
                            ) : (
                                <span>No marketplace</span>
                            )}
                        </td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default ListingTable;