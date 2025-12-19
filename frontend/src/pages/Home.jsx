import { useEffect, useState } from "react";
import { apiGet } from "../api/client";

export default function Home() {
  const [quote, setQuote] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    apiGet("/external/quote", null)
      .then(setQuote)
      .catch((e) => setError(e?.message || "Could not load quote"));
  }, []);

  return (
    <div style={{ padding: 16, maxWidth: 1100, margin: "0 auto" }}>
      <h2 style={{ margin: "6px 0 8px" }}>Dashboard</h2>
      <p style={{ marginTop: 0, opacity: 0.75 }}>
        Quick flow: <b>Rooms</b> → pick a room → choose a time slot → select an
        event → reserve. Then manage it in <b>My Reservations</b>.
      </p>

      <div
        style={{
          marginTop: 14,
          padding: 14,
          borderRadius: 14,
          border: "1px solid rgba(0,0,0,0.08)",
          background: "rgba(0,0,0,0.02)",
        }}
      >
        <h3 style={{ margin: "0 0 8px" }}>External API (Demo)</h3>
        {error && <p style={{ color: "crimson" }}>{error}</p>}
        {!error && !quote && <p>Loading…</p>}
        {quote && (
          <>
            <p style={{ margin: "8px 0" }}>
              <i>“{quote.content ?? quote.quote ?? JSON.stringify(quote)}”</i>
            </p>
            {quote.author && (
              <p style={{ margin: 0, opacity: 0.75 }}>— {quote.author}</p>
            )}
          </>
        )}
      </div>
    </div>
  );
}