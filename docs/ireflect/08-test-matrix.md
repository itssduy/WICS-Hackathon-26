# I Reflect MVP Test Matrix

## 1. Test Scope

This matrix verifies:

- core onboarding to session completion flow
- session progression and timer interactions
- free/pro gating behavior
- calendar and streak correctness
- safety interruption behavior
- billing state transitions

## 2. Functional Scenarios

| ID | Scenario | Preconditions | Expected Result |
|---|---|---|---|
| F-01 | Signup success | New username, valid passwords | Account created, JWT issued, redirect to onboarding |
| F-02 | Signup mismatch passwords | Password != confirm | 400 validation error |
| F-03 | Login success | Valid credentials | JWT issued, redirect to reflect |
| F-04 | Protected route unauthenticated | No token | Redirect to login |
| F-05 | Onboarding save | Authenticated user | Profile persisted and retrievable |
| F-06 | Start session | Valid category and quota available | Session created with first prompt |
| F-07 | Next question progression | Active session | Prompt stage advances correctly |
| F-08 | End session completion | Active session with closure | Status completed and summary saved |
| F-09 | Resume open session | Open session exists | Continue card appears and opens correct question |
| F-10 | Session history fetch | Completed sessions exist | Paginated history returns newest first |

## 3. Plan and Quota Scenarios

| ID | Scenario | Preconditions | Expected Result |
|---|---|---|---|
| P-01 | Free refresh within limit | Free user, refreshes < 5 | Refresh succeeds, usage increments |
| P-02 | Free refresh over limit | Free user, refreshes = 5 | `PLAN_LIMIT_REACHED` |
| P-03 | Free sessions/day over limit | Free user, sessionsStarted at max | Start blocked with plan-limit error |
| P-04 | Premium category access free user | Free user selects premium category | Blocked with upgrade prompt |
| P-05 | Premium category access pro user | Pro user selects premium category | Session starts successfully |
| P-06 | Free calendar historical day | Date older than yesterday | Day visible but details locked |
| P-07 | Pro calendar historical day | Pro user same date | Full day details accessible |

## 4. Calendar and Streak Scenarios

| ID | Scenario | Preconditions | Expected Result |
|---|---|---|---|
| C-01 | Month view markers | Sessions exist across month | Correct dates marked with reflection indicators |
| C-02 | Day detail contents | Accessible day selected | Category, summary, emotion, themes shown |
| C-03 | Streak continuity | Consecutive daily sessions | Streak increments correctly |
| C-04 | Streak break | Missing day between sessions | Streak resets correctly |

## 5. Safety Scenarios

| ID | Scenario | Preconditions | Expected Result |
|---|---|---|---|
| S-01 | Neutral free-text response | Active session | Session proceeds normally |
| S-02 | Danger language detected | User input contains self-harm risk phrase | Session transitions to `SAFETY_INTERRUPTED` |
| S-03 | Post-interrupt behavior | Interrupted session | No new prompts returned; resources shown |
| S-04 | End session from safety state | Interrupted session | Session closes safely with clear user guidance |

## 6. Billing Scenarios

| ID | Scenario | Preconditions | Expected Result |
|---|---|---|---|
| B-01 | Checkout session creation | Authenticated free user | Stripe URL returned |
| B-02 | Webhook upgrade event | Valid Stripe signature and paid event | User status -> `PRO_ACTIVE` |
| B-03 | Webhook cancellation event | Valid cancellation event | User status -> `PRO_CANCELED` |
| B-04 | Billing status read | Any authenticated user | Plan/status payload matches stored subscription state |

## 7. Non-Functional Checks

- Responsive layout on mobile, tablet, and desktop for auth, reflect, session, and calendar pages.
- Keyboard accessibility for core controls.
- Contrast and readability in atmospheric gradient surfaces.
- No sensitive reflection text in server logs.
- API p95 latency acceptable for session next/refresh endpoints under demo load.

## 8. Exit Criteria

MVP is demo-ready when:

- all `F-*` scenarios pass
- no blocker failures in `P-*`, `S-*`, or `B-*`
- design fidelity is aligned with the design system doc
- primary flow works without manual DB intervention
