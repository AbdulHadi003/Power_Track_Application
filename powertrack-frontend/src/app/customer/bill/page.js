"use client";


export default function Bills() {
const bills = [
{ id: 'BILL-001', month: 'Oct 2025', amount: 12_350, due: '2025-11-15', status: 'Unpaid' },
{ id: 'BILL-002', month: 'Sep 2025', amount: 8_200, due: '2025-10-15', status: 'Paid' },
];


return (
<div className="min-h-screen bg-gray-100 p-6">
<h2 className="text-2xl font-semibold mb-4">Your Bills</h2>


<div className="space-y-4 max-w-3xl mx-auto">
{bills.map((b) => (
<div key={b.id} className="bg-white rounded-2xl shadow p-4 flex justify-between items-center">
<div>
<div className="text-sm text-gray-500">{b.month} • {b.id}</div>
<div className="text-lg font-semibold">PKR {b.amount.toLocaleString()}</div>
<div className="text-sm text-gray-500">Due: {b.due}</div>
</div>


<div className="text-right">
<div className={`px-3 py-1 rounded-full text-sm ${b.status === 'Paid' ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'}`}>{b.status}</div>
</div>
</div>
))}
</div>
</div>
);
}