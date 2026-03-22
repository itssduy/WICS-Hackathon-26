import { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router';
import { useAuth } from '../context/AuthContext';
import './AuthPages.css';

export default function Login() {
  const [usernameOrEmail, setUsernameOrEmail] = useState('');
  const [password, setPassword] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(usernameOrEmail, password);
      const redirectTo = location.state?.from?.pathname || '/reflect';
      navigate(redirectTo, { replace: true });
    } catch (err) {
      setError(err.message || 'Login failed. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      {/* Left branded panel */}
      <div className="auth-brand-panel">
        <div className="auth-brand-bg">
          <img src="/background.png" alt="" className="auth-brand-img" />
          <div className="auth-brand-overlay" />
        </div>
        <div className="auth-brand-content fade-in-up">
          <Link to="/" className="auth-brand-logo">
            <img src="/logo.png" alt="I Reflect" className="auth-logo-img" />
            <span>I Reflect</span>
          </Link>
          <h1 className="auth-brand-title">Return to your quiet place.</h1>
          <p className="auth-brand-desc">
            Welcome back. Step into your personal sanctuary and continue your journey toward
            calmer, more intentional reflection.
          </p>
          <ul className="auth-features">
            <li>
              <span className="auth-feature-icon">✨</span>
              Guided reflection, one question at a time
            </li>
            <li>
              <span className="auth-feature-icon">🔒</span>
              Private and personal space
            </li>
            <li>
              <span className="auth-feature-icon">🌿</span>
              Built to help you slow down and feel lighter
            </li>
          </ul>
        </div>
      </div>

      {/* Right form panel */}
      <div className="auth-form-panel">
        <form className="auth-form glass-card-strong soft-bloom" onSubmit={onSubmit} id="login-form">
          <div className="auth-form-header">
            <h2>Welcome back</h2>
            <p>Log in to continue reflecting.</p>
          </div>

          {error && <div className="auth-error" role="alert">{error}</div>}

          <div className="input-group">
            <label className="input-label" htmlFor="login-username">Username or email</label>
            <input
              id="login-username"
              type="text"
              className="input-field"
              placeholder="Enter your username or email"
              value={usernameOrEmail}
              onChange={(e) => setUsernameOrEmail(e.target.value)}
              required
            />
          </div>

          <div className="input-group">
            <label className="input-label" htmlFor="login-password">Password</label>
            <div className="input-password-wrap">
              <input
                id="login-password"
                type={showPassword ? 'text' : 'password'}
                className="input-field"
                placeholder="Enter your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                aria-label="Toggle password visibility"
              >
                {showPassword ? '🙈' : '👁'}
              </button>
            </div>
            <Link to="#" className="forgot-link">Forgot password?</Link>
          </div>

          <button type="submit" className="btn-primary auth-submit" disabled={loading}>
            {loading ? 'Logging in…' : 'Log in'}
          </button>

          <div className="auth-divider">
            <span>or continue with</span>
          </div>

          <button type="button" className="btn-google">
            <svg width="18" height="18" viewBox="0 0 18 18"><path d="M16.51 8H8.98v3h4.3c-.18 1-.74 1.48-1.6 2.04v2.01h2.6a7.8 7.8 0 002.38-5.88c0-.57-.05-.99-.15-1.17z" fill="#4285F4" /><path d="M8.98 17c2.16 0 3.97-.72 5.3-1.94l-2.6-2.01c-.72.46-1.63.78-2.7.78a4.72 4.72 0 01-4.44-3.27H1.87v2.08A8 8 0 008.98 17z" fill="#34A853" /><path d="M4.54 10.56a4.69 4.69 0 010-3.12V5.35H1.87a8 8 0 000 7.3l2.67-2.08z" fill="#FBBC05" /><path d="M8.98 3.58c1.32 0 2.5.45 3.44 1.35L14.84 2.5A7.97 7.97 0 008.98 0 8 8 0 001.87 4.36l2.67 2.08A4.72 4.72 0 018.98 3.58z" fill="#EA4335" /></svg>
            Google
          </button>

          <p className="auth-footer-text">
            Don't have an account? <Link to="/signup" className="auth-link">Create account</Link>
          </p>

          <p className="auth-legal">
            By continuing, you agree to our <Link to="/terms">Terms</Link> and <Link to="/privacy">Privacy Policy</Link>.
          </p>
          <p className="auth-copyright">© 2024 I Reflect. All rights reserved.</p>
        </form>
      </div>
    </div>
  );
}
