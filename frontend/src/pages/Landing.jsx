import { Link } from 'react-router';
import './Landing.css';

const CATEGORIES = [
  {
    slug: 'deep-meaning-thoughts',
    name: 'Deep meaning thoughts',
    description: 'Purpose and identity in one presence. A grounded space for meaning and inner truth.',
    prompts: [
      'What truth have you been quietly carrying?',
      'What are you choosing to move away from?',
      'What moment have you been avoiding?',
    ],
    cta: 'Reflect here',
    gradient: 'linear-gradient(135deg, #5C3D2E 0%, #8F4900 50%, #FE9740 100%)',
  },
  {
    slug: 'trauma-relief-thoughts',
    name: 'Trauma relief thoughts',
    description: 'Gentle encounters, emotional release and healing. Feeling lighter, one thought at a time.',
    prompts: [
      'What moment still lingers in your chest?',
      'What would it feel like to set this down?',
    ],
    cta: 'Explore this path',
    gradient: 'linear-gradient(135deg, #4D5B7D 0%, #6B7FA8 50%, #FEC8BC 100%)',
  },
  {
    slug: 'purely-happy-thoughts',
    name: 'Purely happy thoughts',
    description: 'Gratitude and soft joy. Warm moments that make life quietly beautiful.',
    prompts: [
      'What simple thing in life has been quietly beautiful lately?',
      'What are you uncovering gratitude in lately?',
    ],
    cta: 'Reflect here',
    gradient: 'linear-gradient(135deg, #F59E0B 0%, #FEC8BC 50%, #FFF4F2 100%)',
  },
  {
    slug: 'exciting-thoughts',
    name: 'Exciting thoughts',
    description: 'Dreams and future possibilities. Creative exploration as dreams and future vision.',
    prompts: [
      'What possibility secretly excites you?',
      'What scene even excites you?',
      'What yes secretly excites you?',
    ],
    cta: 'Explore this path',
    gradient: 'linear-gradient(135deg, #814E47 0%, #B67B5C 50%, #FE9740 100%)',
  },
];

const FEATURES = [
  { icon: '✨', text: 'Guided reflection, one question at a time' },
  { icon: '🔒', text: 'Private and personal space for all thoughts' },
  { icon: '🌿', text: 'Built to help you slow down and feel lighter' },
];

export default function Landing() {
  return (
    <div className="landing">
      {/* Hero Section */}
      <section className="hero-section" id="hero">
        <div className="hero-bg">
          <img src="/background.png" alt="" className="hero-bg-img" />
          <div className="hero-bg-overlay" />
        </div>
        <div className="hero-content fade-in-up">
          <h1 className="hero-title">
            Give your thoughts a quiet place to land.
          </h1>
          <p className="hero-subtitle">
            I Reflect helps you slow down, sit with meaningful thoughts, debrief emotions,
            and leave feeling a little lighter. Discover self-happiness through intentional reflection.
          </p>
          <div className="hero-actions">
            <Link to="/reflect" className="btn-primary">Start reflecting</Link>
            <a href="#categories" className="btn-secondary">Explore categories</a>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section className="features-section" id="features">
        <h2 className="section-title">What we are about</h2>
        <div className="features-grid">
          {FEATURES.map((f, i) => (
            <div className="feature-card glass-card" key={i}>
              <div className="feature-icon">{f.icon}</div>
              <p className="feature-text">{f.text}</p>
            </div>
          ))}
        </div>
      </section>

      {/* Categories Section */}
      <section className="categories-section" id="categories">
        {CATEGORIES.map((cat, i) => (
          <div 
            className={`category-showcase ${i % 2 === 0 ? 'layout-left' : 'layout-right'}`} 
            key={cat.slug}
          >
            <div className="category-image-panel" style={{ background: cat.gradient }}>
              <div className="category-image-glow" />
            </div>
            <div className="category-info-panel">
              <h2 className="category-title">{cat.name}</h2>
              <p className="category-desc">{cat.description}</p>
              <ul className="category-prompts">
                {cat.prompts.map((p, j) => (
                  <li key={j}>• {p}</li>
                ))}
              </ul>
              <Link to={`/reflect/${cat.slug}`} className="btn-primary btn-sm category-cta">
                {cat.cta}
              </Link>
            </div>
          </div>
        ))}
      </section>

      {/* Pricing Section */}
      <section className="pricing-section" id="pricing">
        <div className="pricing-intro">
          <h2>Upgrade plan</h2>
          <p>Feature-rich reflection after your sessions. Unlimited experience with the pro plan.</p>
          <Link to="/signup" className="btn-primary">Upgrade to Pro</Link>
        </div>
        <div className="pricing-card glass-card-strong">
          <div className="pricing-header">
            <h3>Pro plan</h3>
            <div className="pricing-amount">
              <span className="price">$20</span>
              <span className="period">/month</span>
            </div>
          </div>
          <ul className="pricing-features">
            <li>✓ Full calendar reflection history</li>
            <li>✓ More refreshes per day</li>
            <li>✓ Access to premium reflection categories</li>
            <li>✓ Deeper session access</li>
          </ul>
          <Link to="/signup" className="btn-primary pricing-cta">Upgrade to Pro</Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="landing-footer">
        <div className="footer-inner">
          <div className="footer-brand">
            <img src="/logo.png" alt="I Reflect" className="footer-logo" />
            <div>
              <span className="footer-brand-name">I Reflect</span>
              <p className="footer-tagline">Your personal sanctuary for reflection.</p>
            </div>
          </div>
          <div className="footer-links">
            <Link to="/privacy">Privacy Policy</Link>
            <Link to="/terms">Terms</Link>
            <Link to="/safety">Contact</Link>
            <Link to="/safety">Help</Link>
          </div>
          <p className="footer-copy">© 2024 I Reflect. All rights reserved.</p>
        </div>
      </footer>
    </div>
  );
}
