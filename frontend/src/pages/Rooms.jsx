import { useEffect, useMemo, useState } from "react";
import { useAuth } from "../auth/AuthContext";
import { apiGet, apiPost, apiTryGet } from "../api/client.js";

export default function Rooms() {
  const { auth } = useAuth();
  const [rooms, setRooms] = useState([]);
  const [selectedRoomId, setSelectedRoomId] = useState(null);
  const [schedules, setSchedules] = useState([]);
  const [selectedScheduleId, setSelectedScheduleId] = useState(null);

  const [events, setEvents] = useState(null); // null => couldn't load
  const [manualEventId, setManualEventId] = useState("");
  const [selectedEventId, setSelectedEventId] = useState("");

  const [msg, setMsg] = useState("");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    setLoading(true);
    setError("");
    apiGet("/rooms", auth)
      .then((list) => {
        setRooms(list || []);
        if (list?.length) setSelectedRoomId(String(list[0].id));
      })
      .catch((e) => setError(e?.message || "Failed to load rooms"))
      .finally(() => setLoading(false));
  }, [auth]);

  useEffect(() => {
    if (!selectedRoomId) return;
    setLoading(true);
    setError("");
    setSelectedScheduleId(null);
    apiGet(`/rooms/${selectedRoomId}/schedules`, auth)
      .then((list) => setSchedules(list || []))
      .catch((e) => setError(e?.message || "Failed to load schedules"))
      .finally(() => setLoading(false));
  }, [auth, selectedRoomId]);

  useEffect(() => {
    // eventId zorunlu; admin endpoint'i deniyoruz. 403 ise manual input'a düşeceğiz.
    apiTryGet("/admin/events", auth).then((list) => {
      if (list) {
        setEvents(list);
        if (list?.length) setSelectedEventId(String(list[0].id));
      } else {
        setEvents(null);
      }
    });
  }, [auth]);

  const selectedRoom = useMemo(
    () => rooms.find((r) => String(r.id) === String(selectedRoomId)) || null,
    [rooms, selectedRoomId]
  );

  async function reserve() {
    setMsg("");
    setError("");

    const roomId = Number(selectedRoomId);
    const scheduleId = Number(selectedScheduleId);
    const eventIdRaw = events ? selectedEventId : manualEventId;
    const eventId = Number(eventIdRaw);

    if (!roomId || !scheduleId || !eventId) {
      setError("Please select room, time slot, and event.");
      return;
    }

    setLoading(true);
    try {
      const created = await apiPost(
        "/reservations",
        { roomId, scheduleId, eventId },
        auth
      );
      setMsg(`Reservation created (ID: ${created?.id ?? ""})`);
    } catch (e) {
      setError(e?.message || "Failed to create reservation");
    } finally {
      setLoading(false);
    }
  }

  return (
    <div style={{ padding: 16, maxWidth: 1100, margin: "0 auto" }}>
      <h2 style={{ margin: "6px 0 12px" }}>Rooms</h2>

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

      <div style={{ display: "grid", gridTemplateColumns: "1fr 1fr", gap: 14, alignItems: "start" }}>
        <section style={{ padding: 14, borderRadius: 14, border: "1px solid rgba(0,0,0,0.08)", background: "white" }}>
          <h3 style={{ margin: "0 0 10px" }}>Select a room</h3>
          {loading && rooms.length === 0 ? (
            <p>Loading…</p>
          ) : (
            <select
              value={selectedRoomId || ""}
              onChange={(e) => setSelectedRoomId(e.target.value)}
              style={{ width: "100%", padding: 10, borderRadius: 12 }}
            >
              {rooms.map((r) => (
                <option key={r.id} value={String(r.id)}>
                  {r.name} — {r.location} — cap {r.capacity}
                </option>
              ))}
            </select>
          )}

          {selectedRoom && (
            <div style={{ marginTop: 12, opacity: 0.85, fontSize: 14 }}>
              <div><b>Type:</b> {selectedRoom.type}</div>
              <div><b>Location:</b> {selectedRoom.location}</div>
              <div><b>Capacity:</b> {selectedRoom.capacity}</div>
            </div>
          )}
        </section>

        <section style={{ padding: 14, borderRadius: 14, border: "1px solid rgba(0,0,0,0.08)", background: "white" }}>
          <h3 style={{ margin: "0 0 10px" }}>Create reservation</h3>

          <label style={{ display: "block", fontSize: 13, opacity: 0.75 }}>Time slot</label>
          <select
            value={selectedScheduleId || ""}
            onChange={(e) => setSelectedScheduleId(e.target.value)}
            style={{ width: "100%", padding: 10, borderRadius: 12, margin: "6px 0 10px" }}
          >
            <option value="" disabled>Select...</option>
            {schedules.map((s) => (
              <option key={s.id} value={String(s.id)}>
                {String(s.startTime).replace("T", " ")} → {String(s.endTime).replace("T", " ")}
              </option>
            ))}
          </select>

          <label style={{ display: "block", fontSize: 13, opacity: 0.75 }}>Event</label>
          {events ? (
            <select
              value={selectedEventId || ""}
              onChange={(e) => setSelectedEventId(e.target.value)}
              style={{ width: "100%", padding: 10, borderRadius: 12, margin: "6px 0 10px" }}
            >
              {events.map((ev) => (
                <option key={ev.id} value={String(ev.id)}>{ev.title}</option>
              ))}
            </select>
          ) : (
            <input
              value={manualEventId}
              onChange={(e) => setManualEventId(e.target.value)}
              placeholder="Enter Event ID (eventId is required)"
              style={{ width: "100%", padding: 10, borderRadius: 12, margin: "6px 0 10px" }}
            />
          )}

          {!events && (
            <p style={{ marginTop: 0, fontSize: 13, opacity: 0.75 }}>
              Note: Event list endpoint is admin-protected. If you log in as ADMIN, dropdown appears.
            </p>
          )}

          <button
            onClick={reserve}
            disabled={loading}
            style={{
              width: "100%",
              padding: 10,
              borderRadius: 12,
              border: "1px solid rgba(0,0,0,0.18)",
              background: "white",
              cursor: "pointer",
            }}
          >
            {loading ? "Working…" : "Reserve"}
          </button>
        </section>
      </div>
    </div>
  );
}