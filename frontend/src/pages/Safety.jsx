export default function Safety() {
  return (
    <div className="page-skeleton" style={{ maxWidth: 700, margin: '0 auto', textAlign: 'left', padding: '64px 24px' }}>
      <h1>Safety & Support</h1>
      <p style={{ marginTop: 24 }}>
        <strong>I Reflect is not therapy.</strong> It is a guided self-reflection tool designed to
        help you slow down and process your thoughts. It is not a substitute for professional
        mental health support.
      </p>
      <h3 style={{ marginTop: 24 }}>If you are in crisis</h3>
      <ul style={{ marginTop: 12, paddingLeft: 20, lineHeight: 2 }}>
        <li><strong>National Suicide Prevention Lifeline:</strong> 988</li>
        <li><strong>Crisis Text Line:</strong> Text HOME to 741741</li>
        <li><strong>International Association for Suicide Prevention:</strong> <a href="https://www.iasp.info/resources/Crisis_Centres/" target="_blank" rel="noreferrer">Find a crisis center</a></li>
        <li><strong>Emergency:</strong> Call 911 or your local emergency number</li>
      </ul>
      <p style={{ marginTop: 24, color: 'var(--text-muted)' }}>
        If our system detects language that suggests you may be in distress, your session will
        pause and you will be directed to these resources. Your safety is our priority.
      </p>
    </div>
  );
}
