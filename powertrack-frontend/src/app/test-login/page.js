'use client';

import { useState } from 'react';
import { authAPI } from '@/lib/api';

export default function TestLoginPage() {
  const [email, setEmail] = useState('admin@example.com');
  const [password, setPassword] = useState('password123');
  const [result, setResult] = useState(null);
  const [user, setUser] = useState(null);

  const handleLogin = async () => {
    try {
      const response = await authAPI.login({ email, password });
      setResult({ success: true, data: response.data });
    } catch (error) {
      setResult({ success: false, error: error.response?.data?.message || error.message });
    }
  };

  const handleGetProfile = async () => {
    try {
      const response = await authAPI.getCurrentUser();
      setUser(response.data);
    } catch (error) {
      setUser({ error: error.message });
    }
  };

  const handleLogout = async () => {
    try {
      await authAPI.logout();
      setResult(null);
      setUser(null);
      alert('Logged out successfully!');
    } catch (error) {
      alert('Logout failed: ' + error.message);
    }
  };

  return (
    <div className="p-8 max-w-2xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Login Integration Test</h1>

      <div className="bg-white p-6 rounded shadow mb-4">
        <h2 className="text-xl font-semibold mb-4">Step 1: Login</h2>
        
        <div className="space-y-3">
          <div>
            <label className="block mb-1">Email:</label>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="w-full px-3 py-2 border rounded"
            />
          </div>

          <div>
            <label className="block mb-1">Password:</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="w-full px-3 py-2 border rounded"
            />
          </div>

          <button
            onClick={handleLogin}
            className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700"
          >
            Test Login
          </button>
        </div>

        {result && (
          <div className={`mt-4 p-4 rounded ${result.success ? 'bg-green-100' : 'bg-red-100'}`}>
            <pre>{JSON.stringify(result, null, 2)}</pre>
          </div>
        )}
      </div>

      <div className="bg-white p-6 rounded shadow mb-4">
        <h2 className="text-xl font-semibold mb-4">Step 2: Get Profile (with Session Cookie)</h2>
        
        <button
          onClick={handleGetProfile}
          className="bg-green-600 text-white px-4 py-2 rounded hover:bg-green-700"
        >
          Test Get Profile
        </button>

        {user && (
          <div className="mt-4 p-4 bg-gray-100 rounded">
            <pre>{JSON.stringify(user, null, 2)}</pre>
          </div>
        )}
      </div>

      <div className="bg-white p-6 rounded shadow">
        <h2 className="text-xl font-semibold mb-4">Step 3: Logout</h2>
        
        <button
          onClick={handleLogout}
          className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
        >
          Test Logout
        </button>
      </div>
    </div>
  );
}
