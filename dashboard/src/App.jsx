import Dashboard from "./pages/Dashboard";
import ListingDetails from "./components/ListingDetails";

function App() {
    const path = window.location.pathname;

    if (path.startsWith("/listings/")) {
        const listingId = path.split("/").pop();

        return (
            <ListingDetails listingId={listingId} />
        );
    }

    return <Dashboard />;
}

export default App;