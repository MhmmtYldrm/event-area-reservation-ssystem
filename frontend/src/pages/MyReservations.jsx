import { useEffect, useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { apiDelete, apiGet } from "../api/client";

export default function MyReservations() {
  const { auth } = useAuth();
  const [items, setItems] = useState([]);
  const [error, setError] = useState("");
  const [msg, setMsg] = useState("");
  const [loading, setLoading] = useState(false);

  async function load() {
    setLoading(true);
    setError("");
    try {
      const list = await apiGet("/reservations/my", auth);
      setItems(list || []);
    } catch (e) {
      setError(e.message);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    load();
  }, [auth]);

  async function cancel(id) {
    setMsg("");
    setError("");
    setLoading(true);
    try {
      await apiDelete(`/reservations/${id}`, auth);
      setMsg(`Reservation ${id} cancelled`);
      await load();
    } catch (e) {
      setError(e?.message || "Cancel failed");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 16, maxWidth: 1100, margin: "0 auto" }}>
      <h2 style={{ margin: "6px 0 12px" }}>My Reservations</h2>

      {error && (
        <div style={{ padding: 10, borderRadius: 12, background: "#fff5f5", border: "1px solid #ffd6d6", marginBottom: 12 }}>
          <b style={{ color: "crimson" }}>Error:</b> {String(error)}
        </div>
      )}
      {msg && (
        <div style={{ padding: 10, borderRadius: 12, background: "#f3fff3", border: "1px solid #c9f0c9", marginBottom: 12 }}>
          {String(msg)}
        </div>
      )}

      <table style={{ width: "100%", borderCollapse: "collapse" }} border="1" cellPadding="8">
        <thead>
          <tr>
            <th>ID</th>
            <th>Room</th>
            <th>Event</th>
            <th>Status</th>
            <th>Start</th>
            <th>End</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {items.map((r) => (
            <tr key={r.id}>
              <td>{r.id}</td>
              <td>{r.roomName}</td>
              <td>{r.eventTitle}</td>
              <td>{r.status}</td>
              <td>{r.startTime}</td>
              <td>{r.endTime}</td>
              <td>
                {r.status === "PENDING" ? (
                  <button
                    onClick={() => cancel(r.id)}
                    disabled={loading}
                    style={{
                      padding: "6px 10px",
                      borderRadius: 10,
                      border: "1px solid rgba(0,0,0,0.18)",
                      background: "white",
                      cursor: "pointer",
                    }}
                  >
                    Cancel
                  </button>
                ) : (
                  <span style={{ opacity: 0.65 }}>—</span>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {loading && <p style={{ marginTop: 12, opacity: 0.75 }}>Working…</p>}
    </div>
  );
}