import { useEffect, useState } from "react";
import Inventory from "./Inventory";
import KpiCards from "../components/KpiCards";
import { getDashboardKpis } from "../services/api";
import ConversationActivity from "../components/ConversationActivity";

function Dashboard() {

    const [kpis, setKpis] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {

        async function loadKpis() {

            try {

                const data = await getDashboardKpis();

                setKpis(data);

            } catch (error) {

                console.error(
                    "Failed to load dashboard KPIs:",
                    error
                );

                setError("Failed to load dashboard KPIs.");

            } finally {

                setLoading(false);
            }
        }

        loadKpis();

    }, []);

    return (
        <div>

            <h1>Aistetic Dashboard</h1>

            {loading && (
                <p>Loading dashboard...</p>
            )}

            {error && (
                <p>{error}</p>
            )}

            {kpis && (
                <KpiCards kpis={kpis} />
            )}

            <Inventory />

            <hr style={{ margin: "40px 0" }} />

            <ConversationActivity conversationId={1} />

        </div>
    );
}

export default Dashboard;