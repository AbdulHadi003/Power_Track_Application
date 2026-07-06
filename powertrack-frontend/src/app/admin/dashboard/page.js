"use client";
import { useRouter } from 'next/navigation';


export default function AdminDashboard(){
const router = useRouter();
const menu = [
{label:'Manage Users', path:'/admin/manageuser'},
{label:'View Profile', path:'/admin/viewprofile'},
{label:'Admiral Functions', path:'/admin/admiralfuctions'},
{label:'Generate Bill', path:'/admin/bills'},
];


return (
<div className="min-h-screen bg-gray-100 p-6">
<div className="flex justify-between items-center mb-6">
<h1 className="text-xl font-semibold">Admin Dashboard</h1>
<button className="bg-red-500 text-white px-4 py-2 rounded-xl">Logout</button>
</div>


<div className="grid gap-4 max-w-lg">
{menu.map(m=> <button key={m.label} onClick={()=>router.push(m.path)} className="bg-white rounded-2xl shadow p-4 text-left">{m.label}</button>)}
</div>
</div>
);
}