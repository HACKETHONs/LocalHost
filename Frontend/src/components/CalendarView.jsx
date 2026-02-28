import { useMemo, useState } from "react";
import Calendar from "react-calendar";
import "react-calendar/dist/Calendar.css";
import { ChevronLeft, ChevronRight, ChevronsLeft, ChevronsRight, X } from "lucide-react";

export default function CalendarView({
  mealPlan,
  loading,
  onMonthChange,
  error,
  selectedDate,
  onDateSelect,
  stickyNotesByDate,
}) {
  const [date, setDate] = useState(selectedDate || new Date());
  const [activeStartDate, setActiveStartDate] = useState(new Date());
  const [isDayModalOpen, setIsDayModalOpen] = useState(false);

  const selectedDayNumber = date.getDate();
  const selectedDateKey = toDateKey(date);
  const stickyNotes = stickyNotesByDate?.[selectedDateKey] || [];
  const selectedDay = useMemo(
    () => mealPlan?.days?.find((day) => day.dayNumber === selectedDayNumber),
    [mealPlan, selectedDayNumber]
  );

  const totals = useMemo(() => {
    if (!selectedDay?.meals) {
      return { protein: 0, calories: 0, minCost: 0 };
    }

    let protein = 0;
    let calories = 0;
    let minCost = 0;

    selectedDay.meals.forEach((meal) => {
      const cheapest = [...meal.options].sort((a, b) => (a.avgCost ?? 0) - (b.avgCost ?? 0))[0];
      if (cheapest) {
        minCost += cheapest.avgCost ?? 0;
        protein += cheapest.protein ?? 0;
        calories += cheapest.calories ?? 0;
      }
    });

    return { protein, calories, minCost };
  }, [selectedDay]);

  const pushMonthChange = (newDate) => {
    const month = newDate.getMonth() + 1;
    const year = newDate.getFullYear();
    onMonthChange?.(month, year);
  };

  const handleDateChange = (newDate) => {
    const singleDate = Array.isArray(newDate) ? newDate[0] : newDate;
    setDate(singleDate);
    onDateSelect?.(singleDate);
    setIsDayModalOpen(true);
  };

  const changeMonth = (offset) => {
    const nextDate = new Date(activeStartDate);
    nextDate.setMonth(nextDate.getMonth() + offset);
    setActiveStartDate(nextDate);
    pushMonthChange(nextDate);
  };

  const changeYear = (offset) => {
    const nextDate = new Date(activeStartDate);
    nextDate.setFullYear(nextDate.getFullYear() + offset);
    setActiveStartDate(nextDate);
    pushMonthChange(nextDate);
  };

  return (
    <div className="w-full flex flex-col items-stretch gap-6">
      <div className="w-full flex items-center justify-between bg-white border-2 border-[#212842] rounded-2xl p-2 shadow-sm">
        <div className="flex gap-1">
          <button onClick={() => changeYear(-1)} className="p-2 hover:bg-[#F4F9FF] rounded-lg transition-colors text-[#212842]">
            <ChevronsLeft size={20} />
          </button>
          <button onClick={() => changeMonth(-1)} className="p-2 hover:bg-[#F4F9FF] rounded-lg transition-colors text-[#212842]">
            <ChevronLeft size={20} />
          </button>
        </div>

        <span className="font-bold text-[#212842] text-lg uppercase tracking-widest">
          {activeStartDate.toLocaleString("default", { month: "long", year: "numeric" })}
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
        onActiveStartDateChange={({ activeStartDate: nextDate }) => {
          if (!nextDate) return;
          setActiveStartDate(nextDate);
          pushMonthChange(nextDate);
        }}
        showNavigation={false}
        tileClassName={({ date: tileDate, view }) => {
          if (view !== "month") return "";
          const hasData = mealPlan?.days?.some((d) => d.dayNumber === tileDate.getDate());
          const key = toDateKey(tileDate);
          const hasSticky = (stickyNotesByDate?.[key]?.length ?? 0) > 0;
          return [hasData ? "has-plan-day" : "", hasSticky ? "has-sticky-note" : ""].join(" ").trim();
        }}
      />

      {loading && (
        <div className="w-full p-4 rounded-xl border-2 border-[#212842] bg-white font-bold text-[#212842]">
          Loading meal plan...
        </div>
      )}

      {error && (
        <div className="w-full p-4 rounded-xl border border-red-300 bg-red-100 text-red-700 font-semibold">
          {error}
        </div>
      )}

      {isDayModalOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setIsDayModalOpen(false)} />
          <div className="relative w-full max-w-4xl max-h-[85vh] overflow-y-auto p-6 bg-[#212842] text-[#F0E7D5] rounded-3xl border-4 border-[#212842] shadow-2xl">
            <div className="flex items-start justify-between gap-4">
              <div>
                <p className="text-xs font-black uppercase tracking-widest opacity-60">Selected Day</p>
                <h3 className="text-3xl font-black">{date.toDateString()}</h3>
              </div>
              <button onClick={() => setIsDayModalOpen(false)} className="p-2 rounded-lg bg-white/10 hover:bg-white/20">
                <X size={20} />
              </button>
            </div>

            <div className="mt-4 grid grid-cols-3 gap-3 w-full md:w-auto">
              <Stat label="Min Cost" value={`Rs ${Math.round(totals.minCost)}`} />
              <Stat label="Protein" value={`${Math.round(totals.protein)} g`} />
              <Stat label="Calories" value={`${Math.round(totals.calories)} kcal`} />
            </div>

            {stickyNotes.length > 0 && (
              <div className="mt-5 rounded-xl border-2 border-[#F0E7D5]/40 bg-[#F0E7D5]/10 p-4">
                <p className="text-xs font-black uppercase tracking-widest opacity-70">Sticky Notes</p>
                <div className="mt-3 grid gap-2 md:grid-cols-2">
                  {stickyNotes.map((note) => (
                    <article key={note.id} className="bg-[#FFF3A3] text-[#212842] rounded-lg p-3 shadow-[3px_3px_0_0_rgba(0,0,0,0.25)]">
                      <p className="text-sm font-bold">{note.name}</p>
                    </article>
                  ))}
                </div>
              </div>
            )}

            {!selectedDay && (
              <p className="mt-5 text-sm font-semibold opacity-80">
                No recipe data available for this date yet. Select another day or regenerate this month.
              </p>
            )}

            {selectedDay && (
              <div className="mt-6 grid gap-4">
                {selectedDay.meals.map((meal) => (
                  <div key={meal.mealType} className="bg-[#F4F9FF] text-[#212842] rounded-2xl p-4 border-2 border-[#212842]">
                    <h4 className="font-black uppercase tracking-wide mb-3">{meal.mealType}</h4>
                    <div className="grid md:grid-cols-2 xl:grid-cols-3 gap-3">
                      {meal.options.map((option) => (
                        <article key={option.id} className="bg-white rounded-xl border border-[#212842]/15 p-3">
                          <p className="font-bold text-sm">{option.name}</p>
                          <div className="mt-2 text-xs font-semibold text-[#212842]/75 flex flex-wrap gap-x-3 gap-y-1">
                            <span>Cost: Rs {option.avgCost ?? "-"}</span>
                            <span>Protein: {Math.round(option.protein ?? 0)} g</span>
                            <span>Calories: {Math.round(option.calories ?? 0)} kcal</span>
                          </div>
                        </article>
                      ))}
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

function Stat({ label, value }) {
  return (
    <div className="bg-[#F0E7D5] text-[#212842] rounded-xl border border-[#F0E7D5] px-3 py-2 text-center">
      <p className="text-[10px] uppercase font-bold tracking-wider opacity-70">{label}</p>
      <p className="font-black text-sm">{value}</p>
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
