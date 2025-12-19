const API_BASE = "http://localhost:8089/api";

/** Login sırasında auth objesi üretir */
export function makeAuth(email, password) {
  const token = btoa(`${email}:${password}`);
  return { email, token };
}

/** Authorization header üretir */
export function authHeader(auth, withJson = true) {
  const h = {};
  if (auth?.token) h.Authorization = `Basic ${auth.token}`;
  if (withJson) h["Content-Type"] = "application/json";
  return h;
}

async function handle(res) {
  if (!res.ok) {
    let txt = "";
    try {
      txt = await res.text();
    } catch {}

    try {
      const maybeJson = JSON.parse(txt);
      throw new Error(maybeJson.message || txt || `HTTP ${res.status}`);
    } catch {
      throw new Error(txt || `HTTP ${res.status}`);
    }
  }

  const ct = res.headers.get("content-type") || "";
  return ct.includes("application/json") ? res.json() : res.text();
}

/** GET */
export function apiGet(path, auth) {
  return fetch(API_BASE + path, {
    headers: authHeader(auth, false),
  }).then(handle);
}

/** POST */
export function apiPost(path, body, auth) {
  return fetch(API_BASE + path, {
    method: "POST",
    headers: authHeader(auth, true),
    body: JSON.stringify(body),
  }).then(handle);
}

/** PATCH */
export function apiPatch(path, body, auth) {
  return fetch(API_BASE + path, {
    method: "PATCH",
    headers: authHeader(auth, true),
    body: JSON.stringify(body),
  }).then(handle);
}

/** DELETE */
export function apiDelete(path, auth) {
  return fetch(API_BASE + path, {
    method: "DELETE",
    headers: authHeader(auth, false),
  }).then(handle);
}

/** Convenience: try GET and return null on 403/404 */
export async function apiTryGet(path, auth) {
  try {
    return await apiGet(path, auth);
  } catch (e) {
    const msg = String(e?.message || "");
    if (msg.includes("403") || msg.toLowerCase().includes("forbidden")) return null;
    if (msg.includes("404") || msg.toLowerCase().includes("not found")) return null;
    throw e;
  }
}