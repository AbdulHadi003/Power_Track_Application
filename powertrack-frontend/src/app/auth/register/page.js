"use client";
import { useState } from "react";


export default function Register() {
const [form, setForm] = useState({
name: "",
email: "",
password: "",
phoneNumber: "",
address: "",
});
const [error, setError] = useState("");


const handleRegister = () => {
for (let key in form) {
if (!form[key]) {
setError("Fill all fields");
return;
}
}
setError("");
};


return (
<div className="flex items-center justify-center min-h-screen bg-gray-100 p-6">
<div className="bg-white rounded-2xl shadow p-6 w-full max-w-md">
<h2 className="text-2xl font-semibold mb-4 text-center">Register</h2>


{error && <p className="text-red-500 text-center mb-3">{error}</p>}


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
onClick={handleRegister}
className="w-full bg-green-600 text-white py-3 rounded-xl hover:bg-green-700"
>
Register
</button>
</div>
</div>
);
}