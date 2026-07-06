"use client";

export default function CsrViewProfile() {
  const user = {
    name: "Ali Khan",
    email: "ali.csr@example.com",
    phone: "+92 300 1111111",
    team: "Field Support",
    employeeId: "CSR-001",
  };

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex flex-col items-center">
      <h2 className="text-2xl font-semibold mb-4">CSR Profile</h2>
      <div className="bg-white rounded-2xl shadow p-6 w-full max-w-2xl">
        <div className="text-lg font-semibold mb-1">{user.name}</div>
        <div className="text-sm text-gray-500 mb-4">{user.team}</div>

        <div className="space-y-3">
          <div className="flex justify-between">
            <div className="text-sm text-gray-600">Email</div>
            <div className="font-medium text-gray-800">{user.email}</div>
          </div>
          <div className="flex justify-between">
            <div className="text-sm text-gray-600">Phone</div>
            <div className="font-medium text-gray-800">{user.phone}</div>
          </div>
          <div className="flex justify-between">
            <div className="text-sm text-gray-600">Employee ID</div>
            <div className="font-medium text-gray-800">{user.employeeId}</div>
          </div>
        </div>
      </div>
    </div>
  );
}
