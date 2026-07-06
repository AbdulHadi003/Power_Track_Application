"use client";
import { useState } from "react";


export default function RequestMeter() {
const [form, setForm] = useState({ name: "", address: "", load: "" });
const [error, setError] = useState("");


const handleRequest = () => {
for (let key in form) {
if (!form[key]) {
setError("Fill all fields");
return;
}
}
setError("");
};


return (
<div className="min-h-screen bg-gray-100 p-6">
<h2 className="text-2xl font-semibold mb-4">Request New Meter</h2>


{error && <p className="text-red-500 mb-3">{error}</p>}


{Object.keys(form).map((field) => (
<input
key={field}
placeholder={field}
value={form[field]}
onChange={(e) => setForm({ ...form, [field]: e.target.value })}
className="w-full mb-3 p-3 rounded-xl border capitalize"
/>
))}


<button
onClick={handleRequest}
className="w-full bg-blue-600 text-white py-3 rounded-xl hover:bg-blue-700"
>
Request
</button>
</div>
);
}