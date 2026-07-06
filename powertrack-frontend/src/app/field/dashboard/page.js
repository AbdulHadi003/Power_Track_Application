"use client";
import { useState } from 'react';


export default function SupportDashboard() {
const [meterNo, setMeterNo] = useState('');
const [reading, setReading] = useState('');
const [error, setError] = useState('');


const submit = () => {
if(!meterNo || !reading) { setError('Enter all fields'); return; }
setError('');
};


return (
<div className="min-h-screen bg-gray-100 p-6">
<div className="flex justify-between items-center mb-6">
<h1 className="text-xl font-semibold">Field Staff Dashboard</h1>
<button className="bg-red-500 text-white px-4 py-2 rounded-xl">Logout</button>
</div>


<div className="bg-white rounded-2xl shadow p-6 max-w-md">
<h2 className="text-lg font-semibold mb-3">Submit Meter Reading</h2>
{error && <div className="text-red-500 mb-3">{error}</div>}
<input placeholder="Meter No" value={meterNo} onChange={e=>setMeterNo(e.target.value)} className="w-full mb-3 p-3 rounded-xl border" />
<input placeholder="Reading" value={reading} onChange={e=>setReading(e.target.value)} className="w-full mb-3 p-3 rounded-xl border" />
<button onClick={submit} className="w-full bg-blue-600 text-white py-3 rounded-xl">Enter</button>
</div>
</div>
);
}