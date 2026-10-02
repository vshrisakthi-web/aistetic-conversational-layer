function KpiCards({ kpis }) {

    const cards = [
        {
            title: "Total Listings",
            value: kpis.totalListings
        },
        {
            title: "Draft Listings",
            value: kpis.draftListings
        },
        {
            title: "Approved Listings",
            value: kpis.approvedListings
        },
        {
            title: "Published Listings",
            value: kpis.publishedListings
        }
    ];

    return (
        <div
            style={{
                display: "grid",
                gridTemplateColumns: "repeat(4, 1fr)",
                gap: "16px",
                marginBottom: "30px"
            }}
        >
            {cards.map((card) => (
                <div
                    key={card.title}
                    style={{
                        border: "1px solid #ddd",
                        borderRadius: "10px",
                        padding: "20px",
                        backgroundColor: "#ffffff"
                    }}
                >
                    <p
                        style={{
                            margin: 0,
                            fontSize: "14px"
                        }}
                    >
                        {card.title}
                    </p>

                    <h2
                        style={{
                            marginTop: "10px",
                            marginBottom: 0
                        }}
                    >
                        {card.value}
                    </h2>
                </div>
            ))}
        </div>
    );
}

export default KpiCards;