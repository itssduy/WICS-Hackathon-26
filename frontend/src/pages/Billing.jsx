import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './Billing.css';

const API_BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

const PRO_FEATURES = [
  { icon: '📅', text: 'Full calendar reflection history — revisit any day' },
  { icon: '🔄', text: 'More refreshes per day (15 vs 5)' },
  { icon: '🌟', text: 'Access to premium reflection categories' },
  { icon: '🧘', text: 'Deeper session access — up to 10 prompts' },
  { icon: '📊', text: 'Advanced insights and mood tracking' },
];

export default function Billing() {
  const { user, token } = useAuth();
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const isPro = user?.plan === 'PRO';

  const handleUpgrade = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await fetch(`${API_BASE}/billing/create-checkout-session`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` },
      });
      if (res.ok) {
        const data = await res.json();
        if (data.checkoutUrl) {
          window.location.href = data.checkoutUrl;
        }
      } else {
        const err = await res.json().catch(() => ({}));
        setError(err.message || 'Could not start checkout');
      }
    } catch {
      setError('Stripe integration is not yet configured.');
    }
    setLoading(false);
  };

  return (
    <div className="billing-page">
      <div className="billing-header fade-in-up">
        <h1>Upgrade your reflection</h1>
        <p>Unlock the full I Reflect experience with Pro.</p>
      </div>

      <div className="billing-layout">
        {/* Current plan */}
        <div className="plan-card glass-card fade-in-up">
          <span className="plan-label">Current plan</span>
          <h2 className="plan-name">{isPro ? 'Pro' : 'Free'}</h2>
          <ul className="plan-limits">
            <li>3 sessions per day</li>
            <li>5 refreshes per day</li>
            <li>7-day calendar access</li>
            <li>4 categories</li>
          </ul>
        </div>

        {/* Pro card */}
        <div className="pro-card glass-card-strong fade-in-up">
          <div className="pro-header">
            <span className="pro-badge">Pro</span>
            <div className="pro-price">
              <span className="pro-amount">$20</span>
              <span className="pro-period">/month</span>
            </div>
          </div>

          <ul className="pro-features">
            {PRO_FEATURES.map((f, i) => (
              <li key={i}>
                <span className="feature-icon">{f.icon}</span>
                <span>{f.text}</span>
              </li>
            ))}
          </ul>

          {error && <p className="billing-error">{error}</p>}

          {isPro ? (
            <div className="pro-active">
              <span className="pro-active-badge">✓ Active</span>
              <p>Your Pro plan is active. Thank you for supporting I Reflect.</p>
            </div>
          ) : (
            <button
              className="btn-primary billing-cta"
              onClick={handleUpgrade}
              disabled={loading}
            >
              {loading ? 'Connecting to Stripe…' : 'Upgrade to Pro'}
            </button>
          )}
        </div>
      </div>
    </div>
  );
}
