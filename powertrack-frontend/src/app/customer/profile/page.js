"use client";


export default function Profile() {
const user = {
name: 'Abdul Hadi',
email: 'hadi@nu.edu.pk',
phone: '+92 300 0000000',
address: 'Malir, Karachi',
meterId: 'MTR-12345',
};


return (
<div className="min-h-screen bg-gray-100 p-6">
<h2 className="text-2xl font-semibold mb-4">Profile</h2>


<div className="bg-white rounded-2xl shadow p-6 max-w-2xl mx-auto">
<div className="mb-4">
<div className="text-2xl font-semibold">{user.name}</div>
<div className="text-sm text-gray-500">Meter ID: {user.meterId}</div>
</div>


<div className="grid grid-cols-1 gap-3">
<div className="flex justify-between">
<div className="text-sm text-gray-600">Email</div>
<div className="font-medium">{user.email}</div>
</div>


<div className="flex justify-between">
<div className="text-sm text-gray-600">Phone</div>
<div className="font-medium">{user.phone}</div>
</div>


<div className="flex justify-between">
<div className="text-sm text-gray-600">Address</div>
<div className="font-medium">{user.address}</div>
</div>
</div>
</div>
</div>
);
}