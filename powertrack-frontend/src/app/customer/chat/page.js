"use client";
import { useState } from "react";


export default function Chat() {
const [messages, setMessages] = useState([
{ id: 1, from: "Support", text: "Welcome to PowerTrack support! How can we help?" },
{ id: 2, from: "You", text: "I need help with my meter reading." },
]);
const [input, setInput] = useState("");


const send = () => {
if (!input.trim()) return; // don't send empty
setMessages((m) => [...m, { id: Date.now(), from: "You", text: input }]);
setInput("");
};


return (
<div className="min-h-screen bg-gray-100 p-6">
<h2 className="text-2xl font-semibold mb-4">Chat with Support</h2>


<div className="bg-white rounded-2xl shadow p-4 max-w-2xl mx-auto flex flex-col" style={{height: '60vh'}}>
<div className="overflow-auto mb-4 flex-1 space-y-3">
{messages.map((m) => (
<div key={m.id} className={`p-3 rounded-xl max-w-[80%] ${m.from === 'You' ? 'bg-blue-50 self-end' : 'bg-gray-50 self-start'}`}>
<div className="text-sm font-medium text-gray-700">{m.from}</div>
<div className="text-base mt-1">{m.text}</div>
</div>
))}
</div>


<div className="flex gap-3">
<input
value={input}
onChange={(e) => setInput(e.target.value)}
placeholder="Type your message..."
className="flex-1 p-3 rounded-xl border"
/>
<button onClick={send} className="px-4 py-2 rounded-xl bg-blue-600 text-white">Send</button>
</div>
</div>
</div>
);
}