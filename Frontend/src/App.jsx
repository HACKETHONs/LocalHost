import { useState } from "react";
import Login from "./pages/Login";
import Dashboard from "./pages/Dashboard";
import { generateMealPlan, loginUser, registerUser } from "./api/backend";

function App() {
  const [user, setUser] = useState(null);
  const [mealPlan, setMealPlan] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const loadMealPlan = async (userId, month, year) => {
    const plan = await generateMealPlan({ userId, month, year });
    setMealPlan(plan);
    return plan;
  };

  const handleAuth = async ({ mode, payload }) => {
    setError("");
    setLoading(true);
    try {
      const authenticatedUser =
        mode === "login" ? await loginUser(payload) : await registerUser(payload);
      const now = new Date();
      await loadMealPlan(authenticatedUser.id, now.getMonth() + 1, now.getFullYear());
      setUser(authenticatedUser);
    } catch (err) {
      setError(err.message || "Unable to authenticate and load plan.");
    } finally {
      setLoading(false);
    }
  };

  const handleMonthChange = async (month, year) => {
    if (!user) return;
    setError("");
    setLoading(true);
    try {
      await loadMealPlan(user.id, month, year);
    } catch (err) {
      setError(err.message || "Unable to load month plan.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {!user ? (
        <Login onLogin={handleAuth} loading={loading} error={error} />
      ) : (
        <Dashboard
          user={user}
          mealPlan={mealPlan}
          loading={loading}
          error={error}
          onMonthChange={handleMonthChange}
        />
      )}
    </>
  );
}

export default App;
