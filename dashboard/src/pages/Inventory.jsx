import { useEffect, useState } from "react";
import { getInventory } from "../services/api";
import ListingTable from "../components/ListingTable";

function Inventory() {
    const [listings, setListings] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function loadInventory() {
            try {
                const data = await getInventory();
                setListings(data);
            } catch (error) {
                console.error("Failed to load inventory:", error);
                setError("Failed to load inventory.");
            } finally {
                setLoading(false);
            }
        }

        loadInventory();
    }, []);

    if (loading) {
        return <p>Loading inventory...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    return (
        <div>
            <h1>Inventory</h1>

            <p>
                Manage listings created through the Aistetic Conversational Layer.
            </p>

            <ListingTable listings={listings} />
        </div>
    );
}

export default Inventory;