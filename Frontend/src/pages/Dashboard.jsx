import { useEffect, useMemo, useState } from "react";
import Header from "../components/Header";
import CalendarView from "../components/CalendarView";
import Chatbot from "../components/Chatbot";

export default function Dashboard({
  user,
  mealPlan,
  loading,
  error,
  onMonthChange,
}) {
  const [isChatOpen, setIsChatOpen] = useState(false);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [stickyNotesByDate, setStickyNotesByDate] = useState({});

  const storageKey = useMemo(
    () => `stickyNotes:${user?.id ?? "guest"}:${mealPlan?.month ?? 0}:${mealPlan?.year ?? 0}`,
    [user?.id, mealPlan?.month, mealPlan?.year]
  );

  useEffect(() => {
    try {
      const raw = localStorage.getItem(storageKey);
      setStickyNotesByDate(raw ? JSON.parse(raw) : {});
    } catch {
      setStickyNotesByDate({});
    }
  }, [storageKey]);

  useEffect(() => {
    try {
      localStorage.setItem(storageKey, JSON.stringify(stickyNotesByDate));
    } catch {
      // ignore storage errors
    }
  }, [storageKey, stickyNotesByDate]);

  const handlePinRecipe = (recipe) => {
    const key = toDateKey(selectedDate);
    setStickyNotesByDate((prev) => {
      const current = prev[key] || [];
      if (current.some((item) => item.id === recipe.id)) {
        return prev;
      }
      return {
        ...prev,
        [key]: [...current, { id: recipe.id, name: recipe.name }],
      };
    });
  };

  return (
    <div className="flex h-screen w-full bg-[#F0E7D5] overflow-hidden">
      <div className="flex flex-col w-full">
        <Header userName={user.name} userGoal={user.goal} />

        <main className="flex-1 p-6 flex items-start overflow-y-auto">
          <CalendarView
            mealPlan={mealPlan}
            loading={loading}
            onMonthChange={onMonthChange}
            error={error}
            selectedDate={selectedDate}
            onDateSelect={setSelectedDate}
            stickyNotesByDate={stickyNotesByDate}
          />
        </main>
      </div>

      <Chatbot
        isOpen={isChatOpen}
        setIsOpen={setIsChatOpen}
        user={user}
        mealPlan={mealPlan}
        selectedDate={selectedDate}
        onPinRecipe={handlePinRecipe}
      />
    </div>
  );
}

function toDateKey(date) {
  const d = new Date(date);
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, "0");
  const dd = String(d.getDate()).padStart(2, "0");
  return `${yyyy}-${mm}-${dd}`;
}
