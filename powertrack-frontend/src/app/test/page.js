'use client';

import { useState, useEffect } from 'react';
import { feederAPI } from '@/lib/api';

export default function TestPage() {
  const [feeders, setFeeders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchFeeders = async () => {
      try {
        const response = await feederAPI.getAllFeeders();
        setFeeders(response.data);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchFeeders();
  }, []);

  if (loading) return <div className="p-8">Loading...</div>;
  if (error) return <div className="p-8 text-red-600">Error: {error}</div>;

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Frontend-Backend Integration Test</h1>
      
      <div className="bg-green-100 border border-green-400 text-green-700 px-4 py-3 rounded mb-4">
        ✅ Successfully connected to backend!
      </div>

      <h2 className="text-xl font-semibold mb-2">Feeders from Backend:</h2>
      
      <div className="space-y-2">
        {feeders.map((feeder) => (
          <div key={feeder.id} className="bg-white p-4 rounded shadow">
            <p><strong>Code:</strong> {feeder.feederCode}</p>
            <p><strong>Name:</strong> {feeder.feederName}</p>
            <p><strong>Area:</strong> {feeder.area}</p>
            <p><strong>Status:</strong> {feeder.status}</p>
          </div>
        ))}
      </div>
    </div>
  );
}