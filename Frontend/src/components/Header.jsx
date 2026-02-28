import { UserCircle } from 'lucide-react';

export default function Header({ userName, userGoal }) {
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

        <div />
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
