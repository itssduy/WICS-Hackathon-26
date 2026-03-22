import { Link, useLocation } from 'react-router';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

export default function Navbar() {
  const { isAuthenticated, logout } = useAuth();
  const location = useLocation();

  // Hide navbar during active reflection session
  if (location.pathname.startsWith('/reflect/')) return null;

  const isActive = (path) => location.pathname === path ? 'nav-link active' : 'nav-link';

  return (
    <nav className="navbar" id="main-nav">
      <div className="nav-inner">
        <Link to="/" className="nav-brand">
          <img src="/logo.png" alt="I Reflect" className="nav-logo" />
          <span className="nav-brand-text">I Reflect</span>
        </Link>

        <div className="nav-links">
          <Link to="/" className={isActive('/')}>Home</Link>
          <Link to="/reflect" className={isActive('/reflect')}>Reflect</Link>
          <Link to="/calendar" className={isActive('/calendar')}>Calendar</Link>
        </div>

        <div className="nav-actions">
          {isAuthenticated ? (
            <>
              <Link to="/profile" className="btn-ghost">Profile</Link>
              <button onClick={logout} className="btn-ghost">Log out</button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-ghost">Log in</Link>
              <Link to="/signup" className="btn-primary btn-sm">Start reflecting</Link>
            </>
          )}
        </div>
      </div>
    </nav>
  );
}
