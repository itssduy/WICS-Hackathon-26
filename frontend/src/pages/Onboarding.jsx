import { useState } from 'react';
import { useNavigate } from 'react-router';
import { useAuth } from '../context/AuthContext';
import './Onboarding.css';

const TONES = ['Gentle', 'Deep', 'Encouraging', 'Calm', 'Direct-kind'];
const MOODS = ['Peaceful', 'Anxious', 'Hopeful', 'Heavy', 'Curious', 'Neutral'];
const TOPICS_HELP = ['Stress', 'Purpose', 'Relationships', 'Identity', 'Grief', 'Motivation', 'Self-worth', 'Change'];
const TOPICS_AVOID = ['Family', 'Trauma', 'Work', 'Health', 'Romance', 'Loss'];
const LIFE_STATUS = ['Student', 'Working professional', 'Between things', 'Parent', 'Retired', 'Other'];

export default function Onboarding() {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [step, setStep] = useState(0);
  const [form, setForm] = useState({
    displayName: '',
    ageOrAgeRange: '',
    gender: '',
    statusInLife: '',
    preferredTone: 'gentle',
    currentMood: 'Neutral',
    primaryInterestCategory: 'deep-meaning-thoughts',
    topicsHelpWith: [],
    topicsToAvoid: [],
  });
  const [loading, setLoading] = useState(false);

  const toggleChip = (field, value) => {
    setForm(prev => {
      const arr = prev[field];
      return {
        ...prev,
        [field]: arr.includes(value) ? arr.filter(v => v !== value) : [...arr, value],
      };
    });
  };

  const handleSubmit = async () => {
    setLoading(true);
    const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';
    try {
      await fetch(`${API_BASE}/profile`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify(form),
      });
      navigate('/reflect');
    } catch {
      navigate('/reflect'); // proceed even if profile save fails for now
    } finally {
      setLoading(false);
    }
  };

  const steps = [
    // Step 0: Name & basics
    <div className="onboarding-step fade-in-up" key="basics">
      <h2>Let's get to know you</h2>
      <p className="onboarding-desc">Just a few things so we can personalize your experience.</p>
      <div className="input-group">
        <label className="input-label">What should we call you?</label>
        <input className="input-field" placeholder="Your name" value={form.displayName}
          onChange={e => setForm({...form, displayName: e.target.value})} />
      </div>
      <div className="input-group">
        <label className="input-label">Age or age range</label>
        <input className="input-field" placeholder="e.g. 22 or 18-24" value={form.ageOrAgeRange}
          onChange={e => setForm({...form, ageOrAgeRange: e.target.value})} />
      </div>
      <div className="input-group">
        <label className="input-label">Where are you in life right now?</label>
        <div className="chip-grid">
          {LIFE_STATUS.map(s => (
            <button key={s} type="button"
              className={`chip ${form.statusInLife === s ? 'chip-active' : ''}`}
              onClick={() => setForm({...form, statusInLife: s})}>
              {s}
            </button>
          ))}
        </div>
      </div>
    </div>,

    // Step 1: Mood & tone
    <div className="onboarding-step fade-in-up" key="mood">
      <h2>How are you feeling?</h2>
      <p className="onboarding-desc">This helps us choose the right starting tone.</p>
      <div className="input-group">
        <label className="input-label">Current mood</label>
        <div className="chip-grid">
          {MOODS.map(m => (
            <button key={m} type="button"
              className={`chip ${form.currentMood === m ? 'chip-active' : ''}`}
              onClick={() => setForm({...form, currentMood: m})}>
              {m}
            </button>
          ))}
        </div>
      </div>
      <div className="input-group">
        <label className="input-label">Preferred tone</label>
        <div className="chip-grid">
          {TONES.map(t => (
            <button key={t} type="button"
              className={`chip ${form.preferredTone === t.toLowerCase() ? 'chip-active' : ''}`}
              onClick={() => setForm({...form, preferredTone: t.toLowerCase()})}>
              {t}
            </button>
          ))}
        </div>
      </div>
    </div>,

    // Step 2: Topics
    <div className="onboarding-step fade-in-up" key="topics">
      <h2>What would you like to explore?</h2>
      <p className="onboarding-desc">Choose topics you'd like help with, and any to avoid.</p>
      <div className="input-group">
        <label className="input-label">Topics I'd like help with</label>
        <div className="chip-grid">
          {TOPICS_HELP.map(t => (
            <button key={t} type="button"
              className={`chip ${form.topicsHelpWith.includes(t.toLowerCase()) ? 'chip-active' : ''}`}
              onClick={() => toggleChip('topicsHelpWith', t.toLowerCase())}>
              {t}
            </button>
          ))}
        </div>
      </div>
      <div className="input-group">
        <label className="input-label">Topics to avoid</label>
        <div className="chip-grid">
          {TOPICS_AVOID.map(t => (
            <button key={t} type="button"
              className={`chip chip-avoid ${form.topicsToAvoid.includes(t.toLowerCase()) ? 'chip-avoid-active' : ''}`}
              onClick={() => toggleChip('topicsToAvoid', t.toLowerCase())}>
              {t}
            </button>
          ))}
        </div>
      </div>
    </div>,
  ];

  return (
    <div className="onboarding-page">
      <div className="onboarding-bg">
        <img src="/background.png" alt="" className="onboarding-bg-img" />
        <div className="onboarding-bg-overlay" />
      </div>
      <div className="onboarding-container glass-card-strong">
        {/* Progress */}
        <div className="onboarding-progress">
          {steps.map((_, i) => (
            <div key={i} className={`progress-dot ${i <= step ? 'progress-dot-active' : ''}`} />
          ))}
        </div>

        {steps[step]}

        <div className="onboarding-actions">
          {step > 0 && (
            <button className="btn-ghost" onClick={() => setStep(step - 1)}>Back</button>
          )}
          {step < steps.length - 1 ? (
            <button className="btn-primary" onClick={() => setStep(step + 1)}>Continue</button>
          ) : (
            <button className="btn-primary" onClick={handleSubmit} disabled={loading}>
              {loading ? 'Saving…' : 'Start reflecting'}
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
