export default function Terms() {
  return (
    <div className="page-skeleton" style={{ maxWidth: 700, margin: '0 auto', textAlign: 'left', padding: '64px 24px' }}>
      <h1>Terms of Service</h1>
      <p style={{ marginTop: 24 }}>
        By using I Reflect, you agree to use this product for personal self-reflection only.
        This product is not a substitute for professional therapy or medical advice.
      </p>
      <p style={{ marginTop: 16 }}>
        You are responsible for maintaining the security of your account credentials.
        We reserve the right to suspend accounts that violate these terms.
      </p>
      <p style={{ marginTop: 16, fontStyle: 'italic', color: 'var(--text-muted)' }}>
        If you are experiencing a crisis, please contact your local emergency services. 
        National Suicide Prevention Lifeline: 988 | Crisis Text Line: Text HOME to 741741
      </p>
    </div>
  );
}
