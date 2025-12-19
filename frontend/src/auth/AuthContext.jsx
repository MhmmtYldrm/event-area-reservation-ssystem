import { createContext, useContext, useMemo, useState } from "react";
import { apiPost, makeAuth } from "../api/client";

const AuthCtx = createContext(null);

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(() => {
    const raw = localStorage.getItem("auth");
    return raw ? JSON.parse(raw) : null;
  });

  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");

  const value = useMemo(() => {
    async function login(email, password) {
      setBusy(true);
      setError("");
      try {
        const a = makeAuth(email, password);
        await apiPost("/auth/login", { email, password }, a); // doğrulama
        setAuth(a);
        localStorage.setItem("auth", JSON.stringify(a));
        return true;
      } catch (e) {
        setError(e?.message || "Login failed");
        return false;
      } finally {
        setBusy(false);
      }
    }

    async function register(fullName, email, password) {
      setBusy(true);
      setError("");
      try {
        await apiPost("/auth/register", { fullName, email, password }, null);
        return await login(email, password); // auto login
      } catch (e) {
        setError(e?.message || "Register failed");
        return false;
      } finally {
        setBusy(false);
      }
    }

    function logout() {
      setAuth(null);
      localStorage.removeItem("auth");
    }

    return { auth, busy, error, setError, login, register, logout };
  }, [auth]);

  return <AuthCtx.Provider value={value}>{children}</AuthCtx.Provider>;
}

export function useAuth() {
  return useContext(AuthCtx);
}