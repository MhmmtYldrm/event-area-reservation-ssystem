import { Routes, Route, Navigate, Outlet } from "react-router-dom";

import Login from "./auth/Login.jsx";
import Register from "./auth/Register.jsx";

import Home from "./pages/Home.jsx";
import Rooms from "./pages/Rooms.jsx";
import MyReservations from "./pages/MyReservations.jsx";

import { useAuth } from "./auth/AuthContext";
import Navbar from "./components/Navbar";

/* 🔒 Korumalı Layout */
function ProtectedLayout() {
  const { auth } = useAuth();

  if (!auth) {
    return <Navigate to="/login" replace />;
  }

  return (
    <>
      <Navbar />
      <Outlet />
    </>
  );
}

export default function App() {
  const { auth } = useAuth();

  return (
    <Routes>
      {/* Public routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/register" element={<Register />} />

      {/* Protected routes */}
      <Route element={<ProtectedLayout />}>
        <Route path="/" element={<Home />} />
        <Route path="/rooms" element={<Rooms />} />
        <Route path="/my-reservations" element={<MyReservations />} />
      </Route>

      {/* Catch-all */}
      <Route
        path="*"
        element={<Navigate to={auth ? "/" : "/login"} replace />}
      />
    </Routes>
  );
}