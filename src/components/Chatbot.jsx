import { X, MessageCircle } from 'lucide-react';

export default function Chatbot({ isOpen, setIsOpen }) {
  if (!isOpen) return (
    <button onClick={() => setIsOpen(true)} className="fixed bottom-10 right-10 p-5 bg-[#FF6F61] text-white rounded-full shadow-xl hover:scale-110 transition-all z-50">
      <MessageCircle size={32} />
    </button>
  );

  return (
    <aside className="bg-[#F4F9FF] border-l-4 border-[#212842] w-1/4 h-full flex flex-col transition-all">
      <div className="p-6 bg-white border-b-2 border-[#212842] flex justify-between items-center">
        <span className="font-black uppercase tracking-widest text-[#212842]">MAX</span>
        <X className="cursor-pointer" onClick={() => setIsOpen(false)} />
      </div>
      <div className="flex-1 p-6 space-y-4 overflow-y-auto">
        <div className="bg-[#212842] text-white p-4 rounded-2xl rounded-tl-none text-sm">
          Based on your budget, I recommend prepping oats tonight!
        </div>
      </div>
      <div className="p-4 bg-white border-t-2 border-[#212842]">
        <input type="text" placeholder="Ask Max..." className="w-full bg-[#F4F9FF] border-2 border-[#212842] p-3 rounded-xl outline-none" />
      </div>
    </aside>
  );
}