# I Reflect MVP Product Spec

## 1. Product Definition

**I Reflect** is a guided self-reflection web app that helps users process thoughts through one-question-at-a-time reflection paths, timed silence, and a gentle closure check-in.

This is a structured self-reflection product, not a therapy product.

## 2. Product Goals

- Help users slow down and mentally declutter.
- Deliver meaningful emotional relief through guided question chains.
- Build repeat usage through streaks, calendar history, and gentle routine.
- Keep the experience private, safe, warm, and non-clinical.

## 3. Non-Goals (MVP)

- Clinical support, diagnosis, or crisis counseling.
- Mobile native app.
- Voice mode or generated meditation audio.
- Social features or sharing feeds.
- Advanced prediction systems.

## 4. Target Users

- Debrief users: processing heavy or confusing experiences.
- Peace users: seeking calm reset and mental space.
- Gratitude users: focusing on comforting, positive thought loops.
- Growth users: exploring identity, values, and direction.
- Habit users: recurring daily clarity routine.

## 5. Core MVP Scope

In scope:

- Public: landing, pricing section, login, signup, policy links.
- Authenticated: onboarding, `/reflect`, `/reflect/:categoryName`, `/calendar`, `/history`, `/profile`, `/billing`.
- Reflection engine: category-based prompt banks, 4-7 question sessions, timer, refresh limit, closure check.
- Persistence: session history, summaries, streaks, calendar day detail.
- Plan gating: free vs pro limits.
- Admin: category and prompt template management.

Out of scope:

- Mobile apps.
- Therapist network.
- Realtime dynamic AI memory graphs.
- Wearables.

## 6. Launch Categories

- Deep meaning thoughts
- Trauma relief thoughts
- Purely happy thoughts
- Exciting thoughts

Each category includes opening, deepening, reframing, release, and closure prompt banks.

## 7. Pricing and Access

Free:

- limited daily refreshes (default: 5)
- limited sessions/day (default: 3)
- standard categories only
- calendar detail: today + yesterday
- basic summary depth

Pro ($20/month):

- higher or unlimited refreshes
- premium categories
- full month calendar detail access
- full session history
- deeper recap and insights

## 8. Safety Baseline

Mandatory:

- clear disclaimer: not therapy, not emergency support
- crisis resources route and in-flow redirect
- topic avoidance preferences during onboarding
- self-harm/danger-language detection in user free text
- immediate interruption and support message when risk language is detected
- user can end session at any time

## 9. UX Principles

- Calm, private, premium atmosphere.
- One meaningful question at a time.
- No chatty or noisy dashboard behavior.
- Gentle ceremony in transitions.
- Warm copy and low visual clutter.

## 10. Acceptance Criteria (Product-Level)

A user can:

- understand value from landing page
- sign up, log in, and complete onboarding
- enter `/reflect`, choose category, and start a session
- complete at least 4 guided questions with timer
- refresh question within plan limits
- complete closure check-in and save summary
- revisit eligible day summaries in calendar
- see streak and plan-gated calendar behavior
- encounter clear upgrade prompts when hitting free limits
