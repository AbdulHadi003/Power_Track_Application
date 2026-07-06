"use client";

export default function AdminAdmiralFunctions() {
  const items = [
    { id: 'A-1', text: 'Complaint: Incorrect meter reading' },
    { id: 'A-2', text: 'Request: New meter installation' },
  ];

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h2 className="text-2xl font-semibold mb-4">Admiral Functions</h2>
      <div className="space-y-3 max-w-3xl">
        {items.map(i => (
          <div key={i.id} className="bg-white rounded-2xl shadow p-4">
            <div className="font-semibold">{i.id}</div>
            <div className="text-sm text-gray-600 mt-1">{i.text}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
