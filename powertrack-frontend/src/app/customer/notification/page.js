"use client";


export default function Notification() {
const notes = [
{ id: 1, title: 'Planned Maintenance', text: 'Power outage scheduled on 2025-12-05 in your area.' },
{ id: 2, title: 'New Bill Generated', text: 'Your bill for Oct 2025 is now available.' },
];


return (
<div className="min-h-screen bg-gray-100 p-6">
<h2 className="text-2xl font-semibold mb-4">Notifications</h2>


<div className="space-y-3 max-w-2xl mx-auto">
{notes.map((n) => (
<div key={n.id} className="bg-white rounded-2xl shadow p-4">
<div className="font-semibold">{n.title}</div>
<div className="text-sm text-gray-600 mt-1">{n.text}</div>
</div>
))}
</div>
</div>
);
}