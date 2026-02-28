import { useState } from 'react';
import Header from '../components/Header';
import CalendarView from '../components/CalendarView';
import Chatbot from '../components/Chatbot';

export default function Dashboard({ user }) {
  const [isChatOpen, setIsChatOpen] = useState(false);

  return (
    <div className="flex h-screen w-full bg-[#F0E7D5] overflow-hidden">
      {/* Main Content Area */}
      <div className={`flex flex-col transition-all duration-500 ${isChatOpen ? 'w-3/4' : 'w-full'}`}>
        <Header userName={user.name} userGoal={user.goal} />
        
        <main className="flex-1 p-8 flex justify-center items-start overflow-y-auto">
          <CalendarView />
        </main>
      </div>

      <Chatbot isOpen={isChatOpen} setIsOpen={setIsChatOpen} />
    </div>
  );
}