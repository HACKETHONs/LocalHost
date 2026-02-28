import { useState, useRef, useEffect } from 'react';
import { X, MessageCircle, Sparkles } from 'lucide-react';
import * as backend from '../api/backend';

export default function Chatbot({ isOpen, setIsOpen, user, mealPlan, selectedDate, onPinRecipe }) {
  const [messages, setMessages] = useState([initialMessage()]);
  const [input, setInput] = useState('');
  const [sending, setSending] = useState(false);
  const [selectedRecipe, setSelectedRecipe] = useState(null);
  const containerRef = useRef(null);

  useEffect(() => {
    if (containerRef.current) {
      containerRef.current.scrollTop = containerRef.current.scrollHeight;
    }
  }, [messages]);

  const handleSend = async () => {
    await sendUserMessage(input);
    setInput('');
  };

  const sendUserMessage = async (rawText) => {
    const userText = (rawText || '').trim();
    if (!userText || sending) return;
    setMessages((m) => [...m, { id: crypto.randomUUID(), from: 'user', text: userText }]);
    setSending(true);

    try {
      const res = await backend.sendChatMessage({ message: userText });
      setMessages((m) => [...m, mapResponseToMessage(res)]);
    } catch (err) {
      setMessages((m) => [...m, { id: crypto.randomUUID(), from: 'bot', text: 'Error: ' + err.message }]);
    } finally {
      setSending(false);
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      e.preventDefault();
      handleSend();
    }
  };

  const handleQuickAction = async (action) => {
    if (sending) return;
    if (action.toLowerCase().includes('improve')) {
      setMessages((m) => [...m, { id: crypto.randomUUID(), from: 'user', text: action }]);
      setSending(true);
      try {
        if (!user?.id) {
          setMessages((m) => [...m, { id: crypto.randomUUID(), from: 'bot', text: 'User session missing. Please login again.' }]);
          return;
        }
        const now = new Date();
        const month = mealPlan?.month ?? now.getMonth() + 1;
        const year = mealPlan?.year ?? now.getFullYear();
        const advice = await backend.generateAiAdvice({
          userId: user.id,
          month,
          year,
          days: 7,
          preferences: '',
        });
        setMessages((m) => [
          ...m,
          {
            id: crypto.randomUUID(),
            from: 'bot',
            text: formatAiAdvice(advice),
            quickActions: ['Show recipe options', 'Budget friendly recipes', 'High protein recipes', 'Improve my plan'],
          },
        ]);
      } catch (err) {
        setMessages((m) => [...m, { id: crypto.randomUUID(), from: 'bot', text: 'Error: ' + err.message }]);
      } finally {
        setSending(false);
      }
      return;
    }

    await sendUserMessage(action);
  };

  if (!isOpen) {
    return (
      <button
        onClick={() => setIsOpen(true)}
        className="fixed bottom-6 right-6 p-4 bg-[#FF6F61] text-white rounded-full shadow-xl hover:scale-110 transition-all z-50"
      >
        <MessageCircle size={28} />
      </button>
    );
  }

  return (
    <div className="fixed bottom-6 right-6 w-[340px] max-h-[560px] bg-white rounded-xl shadow-2xl flex flex-col overflow-hidden z-50 border-2 border-[#212842] transition-all duration-200">
      <div className="bg-[#F4F9FF] p-4 flex justify-between items-center border-b border-[#ddd]">
        <div>
          <span className="font-black uppercase tracking-widest text-[#212842]">Max</span>
          <p className="text-xs font-semibold text-[#212842]/70">Interactive recipe assistant</p>
        </div>
        <X className="cursor-pointer" onClick={() => setIsOpen(false)} />
      </div>
      <div ref={containerRef} className="flex-1 p-4 overflow-y-auto space-y-3 bg-[#fafafa]">
        {messages.map((m) => (
          <MessageBubble key={m.id} message={m} onQuickAction={handleQuickAction} onRecipeOpen={setSelectedRecipe} />
        ))}
        {sending && (
          <div className="max-w-[85%] p-3 rounded-xl bg-[#212842] text-white text-sm">
            Thinking...
          </div>
        )}
      </div>
      <div className="p-3 border-t border-[#ddd] bg-white">
        <textarea
          value={input}
          onChange={(e) => setInput(e.target.value)}
          onKeyDown={handleKeyDown}
          placeholder="Ask Max for recipe ideas and options..."
          className="w-full h-14 resize-none border-2 border-[#212842] rounded-xl p-2 outline-none"
        />
        <button
          onClick={handleSend}
          disabled={sending}
          className="mt-2 w-full bg-[#FF6F61] text-white font-bold py-2 rounded-xl disabled:opacity-60 disabled:cursor-not-allowed"
        >
          {sending ? 'Sending...' : 'Send'}
        </button>
      </div>

      {selectedRecipe && (
        <div className="fixed inset-0 z-[60] flex items-center justify-center p-4">
          <div className="absolute inset-0 bg-black/40" onClick={() => setSelectedRecipe(null)} />
          <div className="relative w-full max-w-sm rounded-2xl bg-white border-2 border-[#212842] p-5 shadow-2xl">
            <h3 className="text-lg font-black text-[#212842]">{selectedRecipe.name}</h3>
            <p className="mt-4 text-sm font-semibold text-[#212842]/80">Cost: Rs {selectedRecipe.avgCost ?? '-'}</p>
            <p className="text-sm font-semibold text-[#212842]/80">Protein: {Math.round(selectedRecipe.protein ?? 0)} g</p>
            <p className="text-sm font-semibold text-[#212842]/80">Calories: {Math.round(selectedRecipe.calories ?? 0)} kcal</p>
            <button
              onClick={() => {
                onPinRecipe?.(selectedRecipe);
                setSelectedRecipe(null);
              }}
              className="mt-4 w-full rounded-xl bg-[#FF6F61] text-white font-bold py-2"
            >
              Pin To {formatDateLabel(selectedDate)}
            </button>
            <button
              onClick={() => setSelectedRecipe(null)}
              className="mt-2 w-full rounded-xl bg-[#212842] text-white font-bold py-2"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

function MessageBubble({ message, onQuickAction, onRecipeOpen }) {
  const isBot = message.from === 'bot';
  return (
    <div className={`max-w-[85%] ${isBot ? '' : 'ml-auto'}`}>
      <div className={`p-3 rounded-xl text-sm whitespace-pre-line ${isBot ? 'bg-[#212842] text-white' : 'bg-[#FF6F61]/20 text-[#212842] border border-[#FF6F61]/30'}`}>
        {message.text}
      </div>
      {message.recipes?.length > 0 && (
        <div className="mt-2 grid gap-2">
          {message.recipes.map((recipe) => (
            <button
              key={recipe.id}
              onClick={() => onRecipeOpen(recipe)}
              className="w-full text-left rounded-xl border border-[#212842]/20 bg-white p-3 hover:bg-[#F4F9FF] transition-colors"
            >
              <p className="font-bold text-[#212842] text-sm">{recipe.name}</p>
              <p className="text-xs font-semibold text-[#212842]/75 mt-1">
                Rs {recipe.avgCost ?? '-'} | {Math.round(recipe.protein ?? 0)} g protein | {Math.round(recipe.calories ?? 0)} kcal
              </p>
            </button>
          ))}
        </div>
      )}
      {message.quickActions?.length > 0 && (
        <div className="mt-2 flex flex-wrap gap-2">
          {message.quickActions.map((action) => (
            <button
              key={action}
              onClick={() => onQuickAction(action)}
              className="inline-flex items-center gap-1 rounded-full bg-[#F4F9FF] text-[#212842] border border-[#212842]/20 px-3 py-1.5 text-xs font-bold hover:bg-white"
            >
              <Sparkles size={12} />
              {action}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}

function initialMessage() {
  return {
    id: crypto.randomUUID(),
    from: 'bot',
    text: 'Hi there! Ask me for recipe options, budget-friendly meals, or AI recipe ideas.',
    quickActions: ['Show recipe options', 'Budget friendly recipes', 'High protein recipes', 'Improve my plan'],
  };
}

function mapResponseToMessage(response) {
  return {
    id: crypto.randomUUID(),
    from: 'bot',
    text: response?.answer || 'Sorry, something went wrong.',
    recipes: response?.recipes || [],
    quickActions: response?.quickActions || [],
  };
}

function formatAiAdvice(advice) {
  if (!advice) {
    return 'Unable to generate improvements right now.';
  }
  const recommendation = advice.recommendations?.length ? `Top recommendation: ${advice.recommendations[0]}` : '';
  const tip = advice.costSavingTips?.length ? `Cost tip: ${advice.costSavingTips[0]}` : '';
  return [advice.summary, recommendation, tip].filter(Boolean).join('\n');
}

function formatDateLabel(date) {
  const d = date ? new Date(date) : new Date();
  return d.toLocaleDateString(undefined, { month: 'short', day: 'numeric' });
}
