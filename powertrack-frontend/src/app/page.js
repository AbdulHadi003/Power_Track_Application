"use client";
import Image from "next/image";
import { useRouter } from "next/navigation";


export default function Home() {
const router = useRouter();


return (
<div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 p-6">
<div className="mb-6">
<Image src="/logo.png" width={180} height={180} alt="PTA Logo" />
</div>


<div className="space-y-4 w-full max-w-xs">
<button
onClick={() => router.push("auth/login")}
className="w-full bg-blue-600 text-white py-3 rounded-xl shadow hover:bg-blue-700"
>
Login
</button>


<button
onClick={() => router.push("auth/register")}
className="w-full bg-green-600 text-white py-3 rounded-xl shadow hover:bg-green-700"
>
Register
</button>
</div>
</div>
);
}