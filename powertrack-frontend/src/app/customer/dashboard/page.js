"use client";
import { useRouter } from "next/navigation";


export default function Dashboard() {
const router = useRouter();


const menu = [
{ label: "Request Meter", path: "/customer/requestmeter" },
{ label: "Chat", path: "/customer/chat" },
{ label: "View Bills", path: "/customer/bill" },
{ label: "Notifications", path: "/customer/notification" },
{ label: "View Profile", path: "/customer/profile" },
{ label: "Installment Request", path: "/customer/installmentrequest" },
];


return (
<div className="min-h-screen bg-gray-100 p-6">
<div className="flex justify-between items-center mb-6">
<h1 className="text-xl font-semibold">Customer Dashboard</h1>
<button className="bg-red-500 text-white px-4 py-2 rounded-xl">Logout</button>
</div>


<div className="grid grid-cols-1 gap-4">
{menu.map((item) => (
<button
key={item.label}
onClick={() => router.push(item.path)}
className="w-full bg-white shadow p-4 rounded-xl text-left hover:bg-gray-50"
>
{item.label}
</button>
))}
</div>
</div>
);
}