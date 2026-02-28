const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8081";

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      "Content-Type": "application/json",
      ...(options.headers || {}),
    },
    ...options,
  });

  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("application/json")
    ? await response.json()
    : await response.text();

  if (!response.ok) {
    const message =
      typeof body === "object" && body !== null
        ? body.message || "Request failed"
        : "Request failed";
    throw new Error(message);
  }

  return body;
}

export async function registerUser(payload) {
  return request("/api/users/register", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function loginUser(payload) {
  return request("/api/users/login", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function generateMealPlan(payload) {
  return request("/api/meal-plans/generate", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function generateAiAdvice(payload) {
  return request("/api/meal-plans/generate-ai-advice", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function sendChatMessage(payload) {
  return request("/api/chat", {
    method: "POST",
    body: JSON.stringify(payload),
  });
}

export async function fetchChatRecipeOptions(params = {}) {
  const query = new URLSearchParams();
  if (params.limit) query.set("limit", String(params.limit));
  if (params.query) query.set("query", params.query);
  const suffix = query.toString() ? `?${query.toString()}` : "";
  return request(`/api/chat/recipe-options${suffix}`);
}
