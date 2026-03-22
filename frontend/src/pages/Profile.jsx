import { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import './Profile.css';

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const TONES = ['gentle', 'deep', 'encouraging', 'calm', 'direct-kind'];
const MOODS = ['Calm', 'Anxious', 'Hopeful', 'Sad', 'Energized', 'Neutral', 'Overwhelmed'];

export default function Profile() {
  const { user, token, logout } = useAuth();
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [saved, setSaved] = useState(false);

  // Editable fields
  const [displayName, setDisplayName] = useState('');
  const [preferredTone, setPreferredTone] = useState('gentle');
  const [currentMood, setCurrentMood] = useState('Neutral');

  useEffect(() => {
    const load = async () => {
      try {
        const res = await fetch(`${API_BASE}/profile`, {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        if (res.ok) {
          const data = await res.json();
          setDisplayName(data.displayName || '');
          setPreferredTone(data.preferredTone || 'gentle');
          setCurrentMood(data.currentMood || 'Neutral');
        }
      } catch { /* no profile yet */ }
      setLoading(false);
    };
    load();
  }, [token]);

  const handleSave = async () => {
    setSaving(true);
    try {
      const res = await fetch(`${API_BASE}/profile`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ displayName, preferredTone, currentMood }),
      });
      if (res.ok) {
        setSaved(true);
        setTimeout(() => setSaved(false), 2000);
      }
    } catch { /* error */ }
    setSaving(false);
  };

  if (loading) {
    return (
      <div className="profile-page">
        <div className="dashboard-loading">
          <div className="loading-dots"><span /><span /><span /></div>
        </div>
      </div>
    );
  }

  return (
    <div className="profile-page">
      <div className="profile-header fade-in-up">
        <h1>Your profile</h1>
        <p>Manage your reflection preferences.</p>
      </div>

      <div className="profile-grid">
        {/* Account info */}
        <div className="profile-card glass-card fade-in-up">
          <h3>Account</h3>
          <div className="profile-field">
            <label className="input-label">Username</label>
            <span className="profile-readonly">{user?.username}</span>
          </div>
          <div className="profile-field">
            <label className="input-label">Plan</label>
            <span className="profile-plan-badge">{user?.plan || 'FREE'}</span>
          </div>
        </div>

        {/* Preferences */}
        <div className="profile-card glass-card fade-in-up">
          <h3>Preferences</h3>

          <div className="profile-field">
            <label className="input-label">Display name</label>
            <input
              type="text"
              className="input-field"
              value={displayName}
              onChange={e => setDisplayName(e.target.value)}
              placeholder="How should we greet you?"
            />
          </div>

          <div className="profile-field">
            <label className="input-label">Preferred tone</label>
            <div className="chip-row">
              {TONES.map(t => (
                <button
                  key={t}
                  className={`chip ${preferredTone === t ? 'chip-selected' : ''}`}
                  onClick={() => setPreferredTone(t)}
                >
                  {t}
                </button>
              ))}
            </div>
          </div>

          <div className="profile-field">
            <label className="input-label">Current mood</label>
            <div className="chip-row">
              {MOODS.map(m => (
                <button
                  key={m}
                  className={`chip ${currentMood === m ? 'chip-selected' : ''}`}
                  onClick={() => setCurrentMood(m)}
                >
                  {m}
                </button>
              ))}
            </div>
          </div>

          <div className="profile-actions">
            <button className="btn-primary" onClick={handleSave} disabled={saving}>
              {saving ? 'Saving…' : saved ? '✓ Saved' : 'Save changes'}
            </button>
          </div>
        </div>

        {/* Actions */}
        <div className="profile-card glass-card fade-in-up">
          <h3>Session</h3>
          <button className="btn-ghost profile-logout" onClick={logout}>
            Log out
          </button>
        </div>
      </div>
    </div>
  );
}
