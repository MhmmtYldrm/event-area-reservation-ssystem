import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

export default function Login() {
  const { login, busy, error, setError } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const submit = async (e) => {
    e.preventDefault();
    setError("");
    const ok = await login(email, password);
    if (ok) navigate("/");
  };

  return (
    <div style={{ minHeight: "100vh", display: "grid", placeItems: "center" }}>
      <form
        onSubmit={submit}
        style={{
          width: 380,
          padding: 18,
          borderRadius: 16,
          border: "1px solid rgba(0,0,0,0.12)",
          background: "white",
        }}
      >
        <h2 style={{ margin: "0 0 12px" }}>Login</h2>

        {error && (
          <p style={{ color: "crimson", marginTop: 0 }}>{String(error)}</p>
        )}

        <label style={{ display: "block", fontSize: 13, opacity: 0.8 }}>
          Email
        </label>
        <input
          placeholder="mail@example.com"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          type="email"
          required
          style={{ width: "100%", padding: 10, borderRadius: 12, margin: "6px 0 10px" }}
        />

        <label style={{ display: "block", fontSize: 13, opacity: 0.8 }}>
          Password
        </label>
        <input
          type="password"
          placeholder="••••••••"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
          style={{ width: "100%", padding: 10, borderRadius: 12, margin: "6px 0 14px" }}
        />

        <button
          type="submit"
          disabled={busy}
          style={{
            width: "100%",
            padding: 10,
            borderRadius: 12,
            border: "1px solid rgba(0,0,0,0.18)",
            background: "white",
            cursor: "pointer",
          }}
        >
          {busy ? "Signing in…" : "Login"}
        </button>

        <p style={{ margin: "12px 0 0", fontSize: 13, opacity: 0.8 }}>
          No account? <Link to="/register">Register</Link>
        </p>
      </form>
    </div>
  );
}