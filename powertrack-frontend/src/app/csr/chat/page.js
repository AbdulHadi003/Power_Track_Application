"use client";

import { useState, useRef, useEffect } from "react";

export default function CsrChat() {
  const [msgs, setMsgs] = useState([
    { id: 1, from: "Customer", text: "Is my meter scheduled for inspection?" },
  ]);
  const [msg, setMsg] = useState("");
  const bottomRef = useRef();

  const sendMsg = () => {
    if (!msg.trim()) return;
    setMsgs((m) => [...m, { id: Date.now(), from: "CSR", text: msg }]);
    setMsg("");
  };

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [msgs]);

  return (
    <div className="min-h-screen bg-gray-100 p-6 flex flex-col items-center">
      <h2 className="text-2xl font-semibold mb-4">CSR Chat</h2>
      <div className="bg-white rounded-2xl shadow p-4 w-full max-w-2xl flex flex-col h-[60vh]">
        <div className="overflow-auto flex-1 space-y-3 mb-4">
          {msgs.map((m) => (
            <div
              key={m.id}
              className={`p-3 rounded-xl max-w-[70%] ${
                m.from === "CSR"
                  ? "self-end bg-blue-50 text-right"
                  : "self-start bg-gray-50"
              }`}
            >
              <div className="text-sm font-medium text-gray-500">{m.from}</div>
              <div className="mt-1 text-gray-800">{m.text}</div>
            </div>
          ))}
          <div ref={bottomRef} />
        </div>
        <div className="flex gap-3">
          <input
            value={msg}
            onChange={(e) => setMsg(e.target.value)}
            placeholder="Reply..."
            className="flex-1 p-3 rounded-xl border focus:outline-none focus:ring-1 focus:ring-blue-400"
          />
          <button
            onClick={sendMsg}
            className="px-4 py-2 rounded-xl bg-blue-600 text-white hover:bg-blue-700 transition"
          >
            Send
          </button>
        </div>
      </div>
    </div>
  );
}
