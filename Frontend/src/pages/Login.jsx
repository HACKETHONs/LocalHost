import { useState } from 'react';

export default function Login({ onLogin }) {
  const [formData, setFormData] = useState({
    name: '', email: '', password: '', age: '',
    height: '', weight: '', mealsPerDay: '',
    budget: '', hasAppliances: 'N',
    goal: '' // Added goal to the state
  });

  const handleSubmit = (e) => {
    e.preventDefault();
    onLogin(formData); // This sends the new 'goal' property to App.jsx
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-[#F0E7D5] p-6">
      <form onSubmit={handleSubmit} className="bg-white border-4 border-[#212842] p-8 rounded-3xl shadow-[12px_12px_0px_0px_#212842] w-full max-w-2xl grid grid-cols-2 gap-4">
        <h2 className="col-span-2 text-3xl font-black uppercase text-[#212842] mb-4 text-center">
  Create Profile
</h2>
        
        {/* Existing Inputs */}
        <input type="text" placeholder="Full Name" required className="col-span-2 p-3 border-2 border-[#212842] rounded-xl" onChange={(e) => setFormData({...formData, name: e.target.value})} />
        
        {/* New Goal Input - High Visibility */}
        <input 
          type="text" 
          placeholder="Enter Your Primary Goal (e.g., Max Macros // Min Expense)" 
          required 
          className="col-span-2 p-3 border-2 border-[#212842] rounded-xl" 
          onChange={(e) => setFormData({...formData, goal: e.target.value})} 
        />

        <input type="email" placeholder="Email" required className="p-3 border-2 border-[#212842] rounded-xl" onChange={(e) => setFormData({...formData, email: e.target.value})} />
        <input type="password" placeholder="Password" required className="p-3 border-2 border-[#212842] rounded-xl" onChange={(e) => setFormData({...formData, password: e.target.value})} />
        
        {/* Other fields remain the same */}
        <input type="number" placeholder="Age" className="p-3 border-2 border-[#212842] rounded-xl" onChange={(e) => setFormData({...formData, age: e.target.value})} />
        <input type="number" placeholder="Height (cm)" className="p-3 border-2 border-[#212842] rounded-xl" onChange={(e) => setFormData({...formData, height: e.target.value})} />
        <input type="number" placeholder="Weight (kg)" className="p-3 border-2 border-[#212842] rounded-xl" onChange={(e) => setFormData({...formData, weight: e.target.value})} />
        <input type="number" placeholder="Meals Per Day" className="p-3 border-2 border-[#212842] rounded-xl" onChange={(e) => setFormData({...formData, mealsPerDay: e.target.value})} />
        
        <button type="submit" className="col-span-2 mt-4 bg-[#FF6F61] text-white font-black py-4 rounded-xl border-2 border-[#212842] shadow-[4px_4px_0px_0px_#212842] hover:translate-y-1 transition-all">
          GET STARTED
        </button>
      </form>
    </div>
  );
}