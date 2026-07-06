"use client";
import { useState } from "react";
import { useRouter } from "next/navigation";


export default function Login() {
const [email, setEmail] = useState("");
const [password, setPassword] = useState("");
const [error, setError] = useState("");
const router = useRouter();


const handleLogin = () => {
if (!email || !password) {
setError("Fill all fields");
return;
}
setError("Wrong credentials");
};


return (
<div className="flex items-center justify-center min-h-screen bg-gray-100 p-6">
<div className="bg-white rounded-2xl shadow p-6 w-full max-w-md">
<h2 className="text-2xl font-semibold mb-4 text-center">Login</h2>


{error && (
<p className="text-red-500 text-center mb-3 font-medium">{error}</p>
)}


<input
type="email"
placeholder="Email"
value={email}
onChange={(e) => setEmail(e.target.value)}
className="w-full mb-3 p-3 rounded-xl border"
/>


<input
type="password"
placeholder="Password"
value={password}
onChange={(e) => setPassword(e.target.value)}
className="w-full mb-3 p-3 rounded-xl border"
/>


<button
onClick={handleLogin}
className="w-full bg-blue-600 text-white py-3 rounded-xl hover:bg-blue-700"
>
Login
</button>
</div>
</div>
);
}