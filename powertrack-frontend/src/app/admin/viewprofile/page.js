"use client";

export default function AdminViewProfile() {
  const admin = { name: 'Admin - Faiza', email: 'faiza.admin@example.com', role: 'System Admin' };

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex flex-col items-center">
      <h2 className="text-2xl font-semibold mb-4">Admin Profile</h2>
      <div className="bg-white rounded-2xl shadow p-6 max-w-2xl w-full">
        <div className="text-lg font-semibold mb-2">{admin.name}</div>
        <div className="text-sm text-gray-600">{admin.role}</div>
        <div className="mt-4 space-y-3">
          <div className="flex justify-between">
            <div className="text-sm text-gray-600">Email</div>
            <div className="font-medium">{admin.email}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
