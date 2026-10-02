import { useEffect, useState } from "react";
import { getListingById } from "../services/api";
import MarketplaceStatus from "./MarketplaceStatus";

function ListingDetails({ listingId }) {
    const [listing, setListing] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        async function loadListing() {
            try {
                setLoading(true);

                const data = await getListingById(listingId);

                setListing(data);
            } catch (err) {
                setError(err.message);
            } finally {
                setLoading(false);
            }
        }

        loadListing();
    }, [listingId]);

    if (loading) {
        return <p>Loading listing...</p>;
    }

    if (error) {
        return <p>Failed to load listing: {error}</p>;
    }

    if (!listing) {
        return <p>Listing not found.</p>;
    }

    return (
        <div>
            <h2>{listing.title}</h2>

            <p>
                <strong>Listing ID:</strong> {listing.id}
            </p>

            <p>
                <strong>Brand:</strong> {listing.brand}
            </p>

            <p>
                <strong>Category:</strong> {listing.category}
            </p>

            <p>
                <strong>Color:</strong> {listing.color}
            </p>

            <p>
                <strong>Size:</strong> {listing.size}
            </p>

            <p>
                <strong>Condition:</strong> {listing.condition}
            </p>

            <p>
                <strong>Price:</strong> ₹{listing.price}
            </p>

            <p>
                <strong>Status:</strong> {listing.status}
            </p>

            <p>
                <strong>Description:</strong> {listing.description}
            </p>
            <h3>Marketplace Publications</h3>

            {listing.marketplacePublications &&
            listing.marketplacePublications.length > 0 ? (
                <div>
                    {listing.marketplacePublications.map((publication) => (
                        <div key={publication.externalListingId}>

                            <MarketplaceStatus
                                marketplace={publication.marketplace}
                                status={publication.status}
                                externalListingId={publication.externalListingId}
                            />

                            <hr />
                        </div>
                    ))}
                </div>
            ) : (
                <p>No marketplace publications.</p>
            )}
        </div>
    );
}

export default ListingDetails;