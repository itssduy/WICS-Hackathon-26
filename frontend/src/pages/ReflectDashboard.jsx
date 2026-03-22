import { useState, useEffect } from 'react';
import { Link } from 'react-router';
import { useAuth } from '../context/AuthContext';
import './ReflectDashboard.css';

const GRADIENT_MAP = {
  'deep-meaning-thoughts': 'linear-gradient(135deg, #5C3D2E, #8F4900)',
  'trauma-relief-thoughts': 'linear-gradient(135deg, #4D5B7D, #6B7FA8)',
  'purely-happy-thoughts': 'linear-gradient(135deg, #F59E0B, #FEC8BC)',
  'exciting-thoughts': 'linear-gradient(135deg, #814E47, #B67B5C)',
};

const FALLBACK_CATEGORIES = [
  {
    slug: 'deep-meaning-thoughts',
    name: 'Deep meaning thoughts',
    description: 'A grounded space for purpose and inner truth.',
    samplePrompts: ['What truth have you been quietly carrying?', 'What part of yourself have you been neglecting lately?'],
    isPremium: false,
  },
  {
    slug: 'trauma-relief-thoughts',
    name: 'Trauma relief thoughts',
    description: 'Gentle encounters to release and feel lighter.',
    samplePrompts: ['What moment still lingers in your chest?', 'What would it feel like to set this down?'],
    isPremium: false,
  },
  {
    slug: 'purely-happy-thoughts',
    name: 'Purely happy thoughts',
    description: 'Gratitude and soft joy.',
    samplePrompts: ['What simple thing has been quietly beautiful?', 'What are you uncovering gratitude in?'],
    isPremium: false,
  },
  {
    slug: 'exciting-thoughts',
    name: 'Exciting thoughts',
    description: 'Dreams, future possibilities, and creative exploration.',
    samplePrompts: ['What possibility secretly excites you?', 'What yes secretly excites you?'],
    isPremium: false,
  },
];

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export default function ReflectDashboard() {
  const { user, token } = useAuth();
  const [categories, setCategories] = useState(FALLBACK_CATEGORIES);
  const [lastSession, setLastSession] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      try {
        const res = await fetch(`${API_BASE}/categories`, {
          headers: token ? { 'Authorization': `Bearer ${token}` } : {},
        });
        if (res.ok) {
          const data = await res.json();
          if (data.length > 0) setCategories(data);
        }
      } catch {
        // Use fallback categories
      }

      // Try to load last session for continue card
      if (token) {
        try {
          const res = await fetch(`${API_BASE}/sessions/latest`, {
            headers: { 'Authorization': `Bearer ${token}` },
          });
          if (res.ok) {
            const data = await res.json();
            if (data && data.status === 'IN_PROGRESS') {
              setLastSession(data);
            }
          }
        } catch {
          // No active session
        }
      }

      setLoading(false);
    };
    load();
  }, [token]);

  const greeting = user?.username
    ? `Welcome back, ${user.username}`
    : 'What kind of reflection calls to you?';

  return (
    <div className="reflect-dashboard">
      <div className="dashboard-header fade-in-up">
        <h1>{greeting}</h1>
        <p>Choose a space that feels right for this moment.</p>
      </div>

      {/* Continue Session Card */}
      {lastSession && (
        <Link to={`/reflect/${lastSession.categorySlug}`} className="continue-card glass-card-strong fade-in-up" id="continue-session">
          <div className="continue-card-left">
            <span className="continue-label">Continue reflecting</span>
            <h3 className="continue-title">{lastSession.categoryName || 'Your session'}</h3>
            <p className="continue-desc">You have an active session. Pick up where you left off.</p>
          </div>
          <div className="continue-card-right">
            <span className="continue-arrow">→</span>
          </div>
        </Link>
      )}

      {loading ? (
        <div className="dashboard-loading">
          <div className="loading-dots">
            <span /><span /><span />
          </div>
        </div>
      ) : (
        <div className="categories-grid">
          {categories.map((cat) => (
            <Link
              to={`/reflect/${cat.slug}`}
              className="category-card glass-card"
              key={cat.slug}
            >
              <div
                className="category-card-gradient"
                style={{ background: GRADIENT_MAP[cat.slug] || 'linear-gradient(135deg, #5C3D2E, #8F4900)' }}
              />
              <div className="category-card-content">
                <h3>{cat.name}</h3>
                <p className="category-card-desc">{cat.description}</p>
                <ul className="category-card-prompts">
                  {(cat.samplePrompts || []).map((p, i) => (
                    <li key={i}>{p}</li>
                  ))}
                </ul>
                {cat.isPremium && <span className="premium-badge">Pro</span>}
                <span className="category-card-cta">Reflect here →</span>
              </div>
            </Link>
          ))}
        </div>
      )}
    </div>
  );
}
