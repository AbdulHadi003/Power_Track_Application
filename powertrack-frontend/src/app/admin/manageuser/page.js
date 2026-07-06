"use client";

export default function AdminManageUser() {
  const users = [
    { id: 'U-001', name: 'TAHSEEN', email: 'hadi@example.com' },
    { id: 'U-002', name: 'HASSAN', email: 'sara@example.com' },
  ];

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h2 className="text-2xl font-semibold mb-4">Manage Users</h2>
      <div className="space-y-3 max-w-3xl">
        {users.map(u => (
          <div key={u.id} className="bg-white rounded-2xl shadow p-4">
            <div className="font-semibold">{u.name} • {u.id}</div>
            <div className="text-sm text-gray-600">{u.email}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
