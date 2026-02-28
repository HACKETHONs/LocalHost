import { UserCircle, Flame } from 'lucide-react';

export default function Header({ userName, userGoal }) {
  const streakCount = 12; // This can eventually come from props

  return (
    <header className="bg-[#212842] text-[#F0E7D5] border-b-2 border-[#212842] p-6 px-12">
      <div className="flex justify-between items-start">
        {/* Left Side: Profile Info */}
        <div className="flex items-center gap-4">
          <UserCircle size={50} strokeWidth={1.5} className="text-[#F0E7D5]" />
          <div>
            <h1 className="text-2xl font-black uppercase tracking-tighter">
              {userName}
            </h1>
            <p className="text-[10px] font-bold uppercase tracking-widest opacity-70">
              User Profile
            </p>
          </div>
        </div>

        {/* Right Side: Streak Bar (Top Right Corner) */}
        <div className="flex flex-col items-end gap-2 w-64">
          <div className="flex items-center gap-2 bg-[#F0E7D5] text-[#212842] px-3 py-1 rounded-xl shadow-sm">
            <Flame size={30} className="text-[#FF6F61]" fill="#FF6F61" />
            <span className="font-white text-[20px] uppercase tracking-tighter">
              {streakCount} Day Streak
            </span>
          </div>
          
        </div>
      </div>

      {/* Bottom Row: Goal Info */}
      <div className="mt-4 pt-4 border-t border-[#F4F9FF]/10">
        <h2 className="text-sm font-bold uppercase tracking-widest">
          Goal: <span className="text-[#FF6F61] ml-2">{userGoal || "Not Set"}</span>
        </h2>
      </div>
    </header>
  );
}