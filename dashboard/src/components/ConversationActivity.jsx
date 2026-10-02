import { useEffect, useState } from "react";
import { getConversationActivity } from "../services/api";

function ConversationActivity({ conversationId }) {
    const [messages, setMessages] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function loadConversationActivity() {
            try {
                setLoading(true);
                setError("");

                const data = await getConversationActivity(conversationId);

                setMessages(data);
            } catch (error) {
                console.error(
                    "Failed to load conversation activity:",
                    error
                );

                setError("Failed to load conversation activity.");
            } finally {
                setLoading(false);
            }
        }

        loadConversationActivity();
    }, [conversationId]);

    if (loading) {
        return <p>Loading conversation activity...</p>;
    }

    if (error) {
        return <p>{error}</p>;
    }

    if (!messages || messages.length === 0) {
        return <p>No conversation activity found.</p>;
    }

    return (
        <div>
            <h2>Conversation Activity</h2>

            {messages.map((message) => (
                <div
                    key={message.messageId}
                    style={{
                        border: "1px solid #ddd",
                        borderRadius: "8px",
                        padding: "12px",
                        marginBottom: "10px"
                    }}
                >
                    <p>
                        <strong>{message.sender}</strong>
                    </p>

                    <p>
                        <strong>Type:</strong>{" "}
                        {message.messageType}
                    </p>

                    <p>
                        {message.content}
                    </p>

                    <small>
                        {new Date(
                            message.createdAt
                        ).toLocaleString()}
                    </small>
                </div>
            ))}
        </div>
    );
}

export default ConversationActivity;