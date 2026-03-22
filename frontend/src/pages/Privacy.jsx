export default function Privacy() {
  return (
    <div className="page-skeleton" style={{ maxWidth: 700, margin: '0 auto', textAlign: 'left', padding: '64px 24px' }}>
      <h1>Privacy Policy</h1>
      <p style={{ marginTop: 24 }}>
        I Reflect is committed to protecting your privacy. Your reflection data is personal and private.
        We do not share your responses with third parties. All session data is stored securely and
        accessible only to you.
      </p>
      <p style={{ marginTop: 16 }}>
        <strong>Data we collect:</strong> username, preferences, session responses, and usage metrics.
      </p>
      <p style={{ marginTop: 16 }}>
        <strong>Data we never sell:</strong> any personal information, reflection content, or behavioral data.
      </p>
      <p style={{ marginTop: 16, fontStyle: 'italic', color: 'var(--text-muted)' }}>
        This is not a therapy product. I Reflect is a structured self-reflection tool. If you are in crisis,
        please contact emergency services or a mental health professional.
      </p>
    </div>
  );
}
