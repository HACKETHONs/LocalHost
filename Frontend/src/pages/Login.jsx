import { useState } from "react";

export default function Login({ onLogin, loading, error }) {
  const [mode, setMode] = useState("signup");
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    age: "",
    heightCm: "",
    weightKg: "",
    mealsPerDay: "3",
    monthlyBudget: "",
    hasCookingAppliance: "false",
    goal: "MAINTAIN",
  });

  const handleChange = (key, value) => {
    setFormData((prev) => ({ ...prev, [key]: value }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (mode === "login") {
      await onLogin({
        mode,
        payload: {
          email: formData.email.trim(),
          password: formData.password,
        },
      });
      return;
    }

    await onLogin({
      mode,
      payload: {
        name: formData.name.trim(),
        email: formData.email.trim(),
        password: formData.password,
        age: Number(formData.age),
        heightCm: Number(formData.heightCm),
        weightKg: Number(formData.weightKg),
        goal: formData.goal,
        mealsPerDay: Number(formData.mealsPerDay),
        monthlyBudget: Number(formData.monthlyBudget),
        hasCookingAppliance: formData.hasCookingAppliance === "true",
      },
    });
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#F0E7D5] p-6">
      <form onSubmit={handleSubmit} className="bg-white border-4 border-[#212842] p-8 rounded-3xl shadow-[12px_12px_0px_0px_#212842] w-full max-w-2xl grid grid-cols-2 gap-4">
        <h2 className="col-span-2 text-3xl font-black uppercase text-[#212842] mb-4 text-center">
          {mode === "signup" ? "Create Profile" : "Login"}
        </h2>

        <div className="col-span-2 flex rounded-xl border-2 border-[#212842] overflow-hidden">
          <button
            type="button"
            onClick={() => setMode("signup")}
            className={`w-1/2 py-2 font-bold ${mode === "signup" ? "bg-[#212842] text-white" : "bg-white text-[#212842]"}`}
          >
            Sign Up
          </button>
          <button
            type="button"
            onClick={() => setMode("login")}
            className={`w-1/2 py-2 font-bold ${mode === "login" ? "bg-[#212842] text-white" : "bg-white text-[#212842]"}`}
          >
            Login
          </button>
        </div>

        {mode === "signup" && (
          <input
            type="text"
            placeholder="Full Name"
            required
            className="col-span-2 p-3 border-2 border-[#212842] rounded-xl"
            value={formData.name}
            onChange={(e) => handleChange("name", e.target.value)}
          />
        )}

        <input
          type="email"
          placeholder="Email"
          required
          className="col-span-2 p-3 border-2 border-[#212842] rounded-xl"
          value={formData.email}
          onChange={(e) => handleChange("email", e.target.value)}
        />

        <input
          type="password"
          placeholder="Password"
          minLength={6}
          required
          className="col-span-2 p-3 border-2 border-[#212842] rounded-xl"
          value={formData.password}
          onChange={(e) => handleChange("password", e.target.value)}
        />

        {mode === "signup" && (
          <>
            <select
              className="col-span-2 p-3 border-2 border-[#212842] rounded-xl bg-white"
              value={formData.goal}
              onChange={(e) => handleChange("goal", e.target.value)}
            >
              <option value="WEIGHT_GAIN">Weight Gain</option>
              <option value="MAINTAIN">Maintain</option>
              <option value="WEIGHT_LOSS">Weight Loss</option>
            </select>

            <input
              type="number"
              placeholder="Age (10-100)"
              min="10"
              max="100"
              required
              className="p-3 border-2 border-[#212842] rounded-xl"
              value={formData.age}
              onChange={(e) => handleChange("age", e.target.value)}
            />
            <input
              type="number"
              placeholder="Height (cm)"
              min="100"
              max="250"
              step="0.1"
              required
              className="p-3 border-2 border-[#212842] rounded-xl"
              value={formData.heightCm}
              onChange={(e) => handleChange("heightCm", e.target.value)}
            />
            <input
              type="number"
              placeholder="Weight (kg)"
              min="25"
              max="300"
              step="0.1"
              required
              className="p-3 border-2 border-[#212842] rounded-xl"
              value={formData.weightKg}
              onChange={(e) => handleChange("weightKg", e.target.value)}
            />
            <input
              type="number"
              placeholder="Meals Per Day (1-6)"
              min="1"
              max="6"
              required
              className="p-3 border-2 border-[#212842] rounded-xl"
              value={formData.mealsPerDay}
              onChange={(e) => handleChange("mealsPerDay", e.target.value)}
            />
            <input
              type="number"
              placeholder="Monthly Budget"
              min="500"
              required
              className="p-3 border-2 border-[#212842] rounded-xl"
              value={formData.monthlyBudget}
              onChange={(e) => handleChange("monthlyBudget", e.target.value)}
            />

            <select
              className="p-3 border-2 border-[#212842] rounded-xl bg-white"
              value={formData.hasCookingAppliance}
              onChange={(e) => handleChange("hasCookingAppliance", e.target.value)}
            >
              <option value="false">No Cooking Appliance</option>
              <option value="true">Has Cooking Appliance</option>
            </select>
            <div className="p-3 rounded-xl border-2 border-dashed border-[#212842]/30 text-xs font-semibold text-[#212842]/75">
              Enter valid profile data to auto-generate your monthly meal calendar.
            </div>
          </>
        )}

        {error && (
          <p className="col-span-2 text-sm font-bold text-red-700 bg-red-100 rounded-xl px-3 py-2 border border-red-200">
            {error}
          </p>
        )}

        <button
          type="submit"
          disabled={loading}
          className="col-span-2 mt-4 bg-[#FF6F61] text-white font-black py-4 rounded-xl border-2 border-[#212842] shadow-[4px_4px_0px_0px_#212842] hover:translate-y-1 transition-all disabled:opacity-70 disabled:cursor-not-allowed"
        >
          {loading ? "PLEASE WAIT..." : mode === "signup" ? "SIGN UP" : "LOGIN"}
        </button>
      </form>
    </div>
  );
}
