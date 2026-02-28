import { useState } from 'react';
import Calendar from 'react-calendar';
import 'react-calendar/dist/Calendar.css';
import { PlusCircle, ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight } from 'lucide-react';

export default function CalendarView() {
  const [date, setDate] = useState(new Date());
  const [activeStartDate, setActiveStartDate] = useState(new Date());
  const [status, setStatus] = useState(null);

  const handleDateChange = (newDate) => {
    setDate(newDate);
    setStatus(newDate.getDate() % 2 === 0 ? "Goal Met" : "Pending");
  };

  // Custom Navigation Functions
  const changeMonth = (offset) => {
    const nextDate = new Date(activeStartDate);
    nextDate.setMonth(nextDate.getMonth() + offset);
    setActiveStartDate(nextDate);
  };

  const changeYear = (offset) => {
    const nextDate = new Date(activeStartDate);
    nextDate.setFullYear(nextDate.getFullYear() + offset);
    setActiveStartDate(nextDate);
  };

  return (
    <div className="w-full flex flex-col items-center gap-8">
      {/* CUSTOM NAVIGATION BAR */}
      <div className="w-full max-w-[950px] flex items-center justify-between bg-white border-2 border-[#212842] rounded-2xl p-2 mb-2 shadow-sm">
        <div className="flex gap-1">
          <button onClick={() => changeYear(-1)} className="p-2 hover:bg-[#F4F9FF] rounded-lg transition-colors text-[#212842]">
            <ChevronsLeft size={20} />
          </button>
          <button onClick={() => changeMonth(-1)} className="p-2 hover:bg-[#F4F9FF] rounded-lg transition-colors text-[#212842]">
            <ChevronLeft size={20} />
          </button>
        </div>

        <span className="font-bold text-[#212842] text-lg uppercase tracking-widest">
          {activeStartDate.toLocaleString('default', { month: 'long', year: 'numeric' })}
        </span>

        <div className="flex gap-1">
          <button onClick={() => changeMonth(1)} className="p-2 hover:bg-[#F4F9FF] rounded-lg transition-colors text-[#212842]">
            <ChevronRight size={20} />
          </button>
          <button onClick={() => changeYear(1)} className="p-2 hover:bg-[#F4F9FF] rounded-lg transition-colors text-[#212842]">
            <ChevronsRight size={20} />
          </button>
        </div>
      </div>

      <Calendar 
        onChange={handleDateChange} 
        value={date} 
        activeStartDate={activeStartDate}
        onActiveStartDateChange={({ activeStartDate: nextDate }) => setActiveStartDate(nextDate)}
        showNavigation={false} // Hides the default bar you wanted to change
      />
      
      {status && (
        <div className="w-full max-w-[950px] p-8 bg-[#212842] text-[#F0E7D5] rounded-3xl border-4 border-[#212842] shadow-[12px_12px_0px_0px_rgba(33,40,66,0.3)]">
          <div className="flex flex-col md:flex-row justify-between items-center gap-6">
            <div>
              <p className="text-sm font-black uppercase tracking-widest opacity-60">Daily Log</p>
              <h3 className="text-3xl font-black italic">{date.toDateString()}</h3>
            </div>
            <div className={`px-6 py-2 rounded-full font-black uppercase text-sm border-2 ${
              status === "Goal Met" ? "bg-[#FF6F61] border-[#F0E7D5]" : "bg-transparent border-amber-400 text-amber-400"
            }`}>
              {status}
            </div>
          </div>

          <div className="mt-6 flex gap-3">
            <button className="flex items-center gap-2 bg-white text-[#212842] px-4 py-2 rounded-lg border-2 border-[#212842] font-bold text-xs hover:bg-[#F4F9FF]">
              <PlusCircle size={16} /> Log Meal
            </button>
            <button className="flex items-center gap-2 bg-white text-[#212842] px-4 py-2 rounded-lg border-2 border-[#212842] font-bold text-xs hover:bg-[#F4F9FF]">
              Track Expense
            </button>
          </div>
        </div>
      )}
    </div>
  );
}