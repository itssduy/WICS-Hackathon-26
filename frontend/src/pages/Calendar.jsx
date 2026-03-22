import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import './Calendar.css';

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const DAY_LABELS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
const MONTH_NAMES = ['January','February','March','April','May','June','July','August','September','October','November','December'];

export default function Calendar() {
  const { token } = useAuth();
  const [year, setYear] = useState(new Date().getFullYear());
  const [month, setMonth] = useState(new Date().getMonth() + 1);
  const [dayMap, setDayMap] = useState({});
  const [streak, setStreak] = useState(null);
  const [selectedDay, setSelectedDay] = useState(null);
  const [dayEntries, setDayEntries] = useState([]);

  // Fetch month data
  useEffect(() => {
    const load = async () => {
      try {
        const res = await fetch(`${API_BASE}/calendar/month?year=${year}&month=${month}`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          const map = {};
          (data.days || []).forEach(d => {
            map[d.date] = d;
          });
          setDayMap(map);
        }
      } catch { /* fallback empty */ }

      try {
        const res = await fetch(`${API_BASE}/streak`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        if (res.ok) setStreak(await res.json());
      } catch { /* fallback */ }

    };
    load();
  }, [year, month, token]);

  // Fetch day detail
  const openDay = async (dateStr) => {
    setSelectedDay(dateStr);
    try {
      const res = await fetch(`${API_BASE}/calendar/day?date=${dateStr}`, {
        headers: { 'Authorization': `Bearer ${token}` },
      });
      if (res.ok) {
        const data = await res.json();
        setDayEntries(data.entries || []);
      }
    } catch { setDayEntries([]); }
  };

  const prevMonth = () => {
    if (month === 1) { setMonth(12); setYear(y => y - 1); }
    else setMonth(m => m - 1);
    setSelectedDay(null);
  };
  const nextMonth = () => {
    if (month === 12) { setMonth(1); setYear(y => y + 1); }
    else setMonth(m => m + 1);
    setSelectedDay(null);
  };

  // Build calendar grid
  const firstDay = new Date(year, month - 1, 1);
  const daysInMonth = new Date(year, month, 0).getDate();
  let startOffset = firstDay.getDay() - 1; // Monday = 0
  if (startOffset < 0) startOffset = 6;

  const today = new Date();
  const todayStr = `${today.getFullYear()}-${String(today.getMonth()+1).padStart(2,'0')}-${String(today.getDate()).padStart(2,'0')}`;

  const cells = [];
  for (let i = 0; i < startOffset; i++) cells.push(null);
  for (let d = 1; d <= daysInMonth; d++) cells.push(d);

  return (
    <div className="calendar-page">
      <div className="calendar-layout">
        {/* Streak sidebar */}
        <aside className="streak-sidebar glass-card fade-in-up">
          <h3 className="streak-title">Your journey</h3>
          <div className="streak-stats">
            <div className="streak-stat">
              <span className="streak-value">{streak?.currentStreak ?? 0}</span>
              <span className="streak-label">Day streak</span>
            </div>
            <div className="streak-stat">
              <span className="streak-value">{streak?.longestStreak ?? 0}</span>
              <span className="streak-label">Longest streak</span>
            </div>
            <div className="streak-stat">
              <span className="streak-value">{streak?.reflectionsThisWeek ?? 0}</span>
              <span className="streak-label">This week</span>
            </div>
            <div className="streak-stat">
              <span className="streak-value">{streak?.reflectionsThisMonth ?? 0}</span>
              <span className="streak-label">This month</span>
            </div>
          </div>
        </aside>

        {/* Calendar grid */}
        <div className="calendar-main">
          <div className="calendar-nav fade-in-up">
            <button className="btn-ghost" onClick={prevMonth}>←</button>
            <h2>{MONTH_NAMES[month - 1]} {year}</h2>
            <button className="btn-ghost" onClick={nextMonth}>→</button>
          </div>

          <div className="calendar-grid glass-card fade-in-up">
            {DAY_LABELS.map(l => (
              <div className="cal-header" key={l}>{l}</div>
            ))}
            {cells.map((day, i) => {
              if (day === null) return <div className="cal-cell cal-empty" key={`e${i}`} />;
              const dateStr = `${year}-${String(month).padStart(2,'0')}-${String(day).padStart(2,'0')}`;
              const hasSession = dayMap[dateStr]?.hasSession;
              const isToday = dateStr === todayStr;
              const isSelected = dateStr === selectedDay;
              const isLocked = dayMap[dateStr]?.isLocked;

              return (
                <button
                  className={`cal-cell ${hasSession ? 'cal-has-session' : ''} ${isToday ? 'cal-today' : ''} ${isSelected ? 'cal-selected' : ''} ${isLocked ? 'cal-locked' : ''}`}
                  key={dateStr}
                  onClick={() => hasSession && openDay(dateStr)}
                  disabled={!hasSession}
                >
                  <span className="cal-day-num">{day}</span>
                  {hasSession && <span className="cal-dot" />}
                  {isLocked && <span className="cal-lock">🔒</span>}
                </button>
              );
            })}
          </div>

          {/* Day detail */}
          {selectedDay && (
            <div className="day-detail glass-card fade-in-up">
              <div className="day-detail-header">
                <h3>{new Date(selectedDay + 'T00:00').toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric' })}</h3>
                <button className="btn-ghost" onClick={() => setSelectedDay(null)}>✕</button>
              </div>
              {dayEntries.length === 0 ? (
                <p className="day-empty">No completed reflections this day.</p>
              ) : (
                dayEntries.map((e, i) => (
                  <div className="day-entry" key={i}>
                    <span className="entry-category">{e.category}</span>
                    {e.finalEmotion && (
                      <span className="entry-emotion">{e.finalEmotion.toLowerCase().replace('_', ' ')}</span>
                    )}
                    <p className="entry-summary">{e.summaryText}</p>
                  </div>
                ))
              )}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
