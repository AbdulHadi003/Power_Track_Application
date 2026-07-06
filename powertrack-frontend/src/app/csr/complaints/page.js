"use client";

export default function CsrComplaints() {
  const complaints = [
    { id: 'C-100', user: 'Hadi', text: 'Incorrect bill amount' },
    { id: 'C-101', user: 'Kamran', text: 'Meter not working' },
  ];

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h2 className="text-2xl font-semibold mb-4">Complaints</h2>
      <div className="space-y-3 max-w-3xl">
        {complaints.map(c => (
          <div key={c.id} className="bg-white rounded-2xl shadow p-4">
            <div className="font-semibold">{c.id} • {c.user}</div>
            <div className="text-sm text-gray-600 mt-1">{c.text}</div>
          </div>
        ))}
      </div>
    </div>
  );
}
