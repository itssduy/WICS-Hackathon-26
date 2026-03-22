import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import './History.css';

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export default function History() {
  const { token } = useAuth();
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await fetch(`${API_BASE}/sessions/history`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          setSessions(Array.isArray(data) ? data : data.content || []);
        }
      } catch { /* fallback empty */ }
      setLoading(false);
    };
    load();
  }, [token]);

  return (
    <div className="history-page">
      <div className="history-header fade-in-up">
        <h1>Reflection history</h1>
        <p>Review your past reflections and insights.</p>
      </div>

      {loading ? (
        <div className="dashboard-loading">
          <div className="loading-dots"><span /><span /><span /></div>
        </div>
      ) : sessions.length === 0 ? (
        <div className="history-empty glass-card fade-in-up">
          <p>No completed reflections yet. Start your first session from the dashboard.</p>
        </div>
      ) : (
        <div className="history-list">
          {sessions.map((s, i) => (
            <div className="history-card glass-card fade-in-up" key={s.id || i}>
              <div className="history-card-header">
                <span className="history-category">{s.categoryName || s.categorySlug}</span>
                <span className="history-date">
                  {s.sessionDate || (s.createdAt && new Date(s.createdAt).toLocaleDateString())}
                </span>
              </div>
              {s.summaryText && <p className="history-summary">{s.summaryText}</p>}
              {s.finalEmotion && (
                <span className="history-emotion">
                  {s.finalEmotion.toLowerCase().replace('_', ' ')}
                </span>
              )}
              <div className="history-meta">
                <span>{s.questionNumber || '?'} prompts</span>
                <span>{s.status?.toLowerCase().replace('_', ' ')}</span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
