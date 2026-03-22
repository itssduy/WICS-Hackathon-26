import { useState, useEffect, useRef } from 'react';
import { useParams, useNavigate } from 'react-router';
import { useAuth } from '../context/AuthContext';
import './ReflectSession.css';

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const TIMER_PRESETS = [3, 5, 10];

export default function ReflectSession() {
  const { categoryName } = useParams();
  const { token } = useAuth();
  const navigate = useNavigate();

  // Session state
  const [session, setSession] = useState(null);
  const [prompt, setPrompt] = useState(null);
  const [status, setStatus] = useState('LOADING'); // LOADING, IN_PROGRESS, COMPLETED, SAFETY_INTERRUPTED
  const [error, setError] = useState('');

  // UI state
  const [responseText, setResponseText] = useState('');
  const [timerSeconds, setTimerSeconds] = useState(0);
  const [timerRunning, setTimerRunning] = useState(false);
  const [showEndModal, setShowEndModal] = useState(false);
  const [safetyData, setSafetyData] = useState(null);
  const [summary, setSummary] = useState(null);
  const [refreshing, setRefreshing] = useState(false);
  const [advancing, setAdvancing] = useState(false);

  // Closure form
  const [finalEmotion, setFinalEmotion] = useState('');
  const [finalCheckin, setFinalCheckin] = useState('');
  const [takeaway, setTakeaway] = useState('');

  const timerRef = useRef(null);

  // Start session on mount
  useEffect(() => {
    const startSession = async () => {
      try {
        const res = await fetch(`${API_BASE}/sessions/start`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
          },
          body: JSON.stringify({ categorySlug: categoryName }),
        });
        const data = await res.json();

        if (!res.ok) {
          setError(data.message || 'Could not start session');
          setStatus('ERROR');
          return;
        }

        setSession(data);
        setPrompt(data.prompt);
        setStatus('IN_PROGRESS');
      } catch {
        setError('Could not connect to server');
        setStatus('ERROR');
      }
    };
    startSession();
    return () => clearInterval(timerRef.current);
  }, [categoryName, token]);

  // Timer logic
  useEffect(() => {
    if (timerRunning && timerSeconds > 0) {
      timerRef.current = setInterval(() => {
        setTimerSeconds(prev => {
          if (prev <= 1) {
            clearInterval(timerRef.current);
            setTimerRunning(false);
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
      return () => clearInterval(timerRef.current);
    }
  }, [timerRunning, timerSeconds]);

  const startTimer = (mins) => {
    setTimerSeconds(mins * 60);
    setTimerRunning(true);
  };

  const formatTimer = () => {
    const m = Math.floor(timerSeconds / 60);
    const s = timerSeconds % 60;
    return `${String(m).padStart(2, '0')}:${String(s).padStart(2, '0')}`;
  };

  // Next question
  const handleNext = async () => {
    if (advancing) return;
    setAdvancing(true);
    try {
      const res = await fetch(`${API_BASE}/sessions/${session.sessionId}/next-question`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({
          responseType: 'REFLECTION_NOTE',
          responseText: responseText,
        }),
      });
      const data = await res.json();

      if (data.status === 'SAFETY_INTERRUPTED') {
        setSafetyData(data);
        setStatus('SAFETY_INTERRUPTED');
        return;
      }

      if (data.status === 'COMPLETED') {
        setSummary(data);
        setStatus('COMPLETED');
        return;
      }

      setPrompt(data.prompt);
      setSession(prev => ({ ...prev, questionNumber: data.questionNumber }));
      setResponseText('');
      setTimerRunning(false);
      setTimerSeconds(0);
    } catch {
      setError('Could not advance question');
    } finally {
      setAdvancing(false);
    }
  };

  // Refresh question
  const handleRefresh = async () => {
    if (refreshing) return;
    setRefreshing(true);
    try {
      const res = await fetch(`${API_BASE}/sessions/${session.sessionId}/refresh-question`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` },
      });
      const data = await res.json();

      if (!res.ok) {
        setError(data.message || 'Could not refresh');
        return;
      }

      setPrompt(data.prompt);
    } catch {
      setError('Could not refresh');
    } finally {
      setRefreshing(false);
    }
  };

  // End session
  const handleEnd = async () => {
    try {
      const res = await fetch(`${API_BASE}/sessions/${session.sessionId}/end`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ finalEmotion, finalCheckin, takeaway }),
      });
      const data = await res.json();

      if (data.status === 'SAFETY_INTERRUPTED') {
        setSafetyData(data);
        setStatus('SAFETY_INTERRUPTED');
        return;
      }

      setSummary(data);
      setStatus('COMPLETED');
      setShowEndModal(false);
    } catch {
      setError('Could not end session');
    }
  };

  // ---- Renders ----

  if (status === 'LOADING') {
    return (
      <div className="session-page">
        <div className="session-loading">
          <div className="loading-dots"><span /><span /><span /></div>
          <p>Preparing your reflection space…</p>
        </div>
      </div>
    );
  }

  if (status === 'ERROR') {
    return (
      <div className="session-page">
        <div className="session-error glass-card-strong">
          <h2>Something went wrong</h2>
          <p>{error}</p>
          <button className="btn-primary" onClick={() => navigate('/reflect')}>Back to categories</button>
        </div>
      </div>
    );
  }

  if (status === 'SAFETY_INTERRUPTED') {
    return (
      <div className="session-page session-safety">
        <div className="safety-card glass-card-strong fade-in-up">
          <div className="safety-icon">🤍</div>
          <h2>We want to make sure you're okay</h2>
          <p className="safety-message">{safetyData?.safetyMessage}</p>
          <div className="safety-resources">
            {safetyData?.crisisResources?.map((r, i) => (
              <div className="resource-item" key={i}>
                <strong>{r.name}</strong>
                <span>{r.contact}</span>
              </div>
            ))}
          </div>
          <div className="safety-actions">
            <button className="btn-primary" onClick={() => navigate('/safety')}>View resources</button>
            <button className="btn-secondary" onClick={() => navigate('/reflect')}>End session</button>
          </div>
        </div>
      </div>
    );
  }

  if (status === 'COMPLETED') {
    return (
      <div className="session-page session-complete">
        <div className="completion-card glass-card-strong fade-in-up">
          <div className="completion-icon">✨</div>
          <h2>Reflection complete</h2>
          <p className="completion-summary">{summary?.summaryText}</p>
          <div className="completion-actions">
            <button className="btn-primary" onClick={() => navigate('/reflect')}>Reflect again</button>
            <button className="btn-secondary" onClick={() => navigate('/calendar')}>View calendar</button>
          </div>
        </div>
      </div>
    );
  }

  // ---- Active session ----
  return (
    <div className="session-page">
      <div className="session-ambient" />

      {/* Header */}
      <div className="session-header">
        <button className="session-back" onClick={() => setShowEndModal(true)} title="End session">
          ✕
        </button>
        <div className="session-progress">
          <span className="session-step">
            {session?.questionNumber || 1} / {session?.questionTotalHint || 5}
          </span>
          <div className="progress-bar">
            <div
              className="progress-fill"
              style={{ width: `${((session?.questionNumber || 1) / (session?.questionTotalHint || 5)) * 100}%` }}
            />
          </div>
        </div>
        <div className="session-stage-badge">
          {prompt?.promptStage?.toLowerCase() || ''}
        </div>
      </div>

      {/* Prompt Cloud */}
      <div className="session-center">
        <div className="prompt-cloud fade-in-up" key={prompt?.promptText}>
          <p className="prompt-text">{prompt?.promptText}</p>
        </div>

        {/* Timer */}
        <div className="session-timer">
          {timerSeconds > 0 ? (
            <div className="timer-display">
              <span className="timer-time">{formatTimer()}</span>
              <button
                className="btn-ghost timer-toggle"
                onClick={() => setTimerRunning(!timerRunning)}
              >
                {timerRunning ? 'Pause' : 'Resume'}
              </button>
            </div>
          ) : (
            <div className="timer-presets">
              {TIMER_PRESETS.map(m => (
                <button key={m} className="timer-preset-btn" onClick={() => startTimer(m)}>
                  {m} min
                </button>
              ))}
            </div>
          )}
        </div>
      </div>

      {/* Response Area */}
      <div className="session-response">
        <textarea
          className="response-textarea"
          placeholder="Write your thoughts here… (optional)"
          value={responseText}
          onChange={(e) => setResponseText(e.target.value)}
          rows={3}
        />
      </div>

      {/* Controls */}
      <div className="session-controls">
        <button
          className="btn-ghost session-ctrl-btn"
          onClick={handleRefresh}
          disabled={refreshing}
          title="Get a different question"
        >
          {refreshing ? '↻' : '↻ Refresh'}
        </button>
        <button
          className="btn-primary session-next-btn"
          onClick={handleNext}
          disabled={advancing}
        >
          {advancing ? 'Loading…' : 'Next →'}
        </button>
        <button
          className="btn-ghost session-ctrl-btn"
          onClick={() => setShowEndModal(true)}
          title="End session"
        >
          End
        </button>
      </div>

      {/* End Session Modal */}
      {showEndModal && (
        <div className="modal-overlay" onClick={() => setShowEndModal(false)}>
          <div className="end-modal glass-card-strong fade-in-up" onClick={e => e.stopPropagation()}>
            <h3>How are you feeling?</h3>
            <p className="modal-desc">Before you go, let's check in.</p>

            <div className="emotion-grid">
              {['LIGHTER', 'RELIEVED', 'CALM', 'STILL_PROCESSING', 'HOPEFUL'].map(e => (
                <button
                  key={e}
                  className={`emotion-btn ${finalEmotion === e ? 'emotion-active' : ''}`}
                  onClick={() => setFinalEmotion(e)}
                >
                  {e.replace('_', ' ').toLowerCase()}
                </button>
              ))}
            </div>

            <textarea
              className="input-field"
              placeholder="Anything you'd like to note? (optional)"
              value={finalCheckin}
              onChange={e => setFinalCheckin(e.target.value)}
              rows={2}
            />

            <textarea
              className="input-field"
              placeholder="What's one takeaway? (optional)"
              value={takeaway}
              onChange={e => setTakeaway(e.target.value)}
              rows={2}
              style={{ marginTop: 12 }}
            />

            <div className="modal-actions">
              <button className="btn-ghost" onClick={() => setShowEndModal(false)}>Cancel</button>
              <button className="btn-primary" onClick={handleEnd}>Complete reflection</button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
