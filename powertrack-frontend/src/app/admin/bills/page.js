"use client";

import { useState, useEffect } from "react";

export default function AdminGenerateBill() {
  const [meterId, setMeterId] = useState("MTR-1001");
  const [tariff, setTariff] = useState("Domestic");
  const [readings, setReadings] = useState("");
  const [error, setError] = useState("");

useEffect(() => {
setTimeout(() => {
    setReadings(Math.floor(Math.random() * 5000 + 100));
}, 0);
}, []);

  const submit = () => {
    if (!meterId || !tariff || readings === "" || readings === null || isNaN(readings)) {
      setError("Enter all fields");
      return;
    }
    setError("");
    // Optional: handle API call here
  };

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex flex-col items-center">
      <h2 className="text-2xl font-semibold mb-4">Generate Bill</h2>
      <div className="w-full max-w-md space-y-3">
        <input
          placeholder="Meter ID"
          value={meterId}
          onChange={e => setMeterId(e.target.value)}
          className="w-full p-3 rounded-xl border"
        />
        <input
          placeholder="Tariff"
          value={tariff}
          onChange={e => setTariff(e.target.value)}
          className="w-full p-3 rounded-xl border"
        />
        <input
          placeholder="Readings"
          value={readings}
          onChange={e => setReadings(e.target.value)}
          className="w-full p-3 rounded-xl border"
        />
        {error && <div className="text-red-500">{error}</div>}
        <button
          onClick={submit}
          className="w-full bg-green-600 text-white py-3 rounded-xl hover:bg-green-700 transition"
        >
          Generate
        </button>
      </div>
    </div>
  );
}
