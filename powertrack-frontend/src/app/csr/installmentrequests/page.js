"use client";

export default function CsrInstallmentRequests() {
  const requests = [
    { id: 'IR-001', user: 'Hadi', bill: 'BILL-001', reason: 'Unemployment', status: 'Pending' }
  ];

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h2 className="text-2xl font-semibold mb-4">Installment Requests</h2>
      <div className="space-y-3 max-w-3xl">
        {requests.map(r => (
          <div key={r.id} className="bg-white rounded-2xl shadow p-4">
            <div className="font-semibold">{r.id} • {r.user}</div>
            <div className="text-sm text-gray-600">Bill: {r.bill} • {r.reason}</div>
            <div className="text-sm text-gray-700 mt-2">Status: {r.status}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
