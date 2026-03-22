# I Reflect App Flow and Route Spec

## 1. Route Map

Public routes:

- `/`
- `/login`
- `/signup`
- `/privacy`
- `/terms`
- `/safety`

Authenticated routes:

- `/onboarding`
- `/reflect`
- `/reflect/:categoryName`
- `/calendar`
- `/history`
- `/profile`
- `/billing`

Admin routes:

- `/admin/categories`
- `/admin/prompts`

## 2. Route Guards

Rules:

- Unauthenticated users attempting authenticated routes are redirected to `/login`.
- Users with incomplete onboarding are redirected to `/onboarding` before `/reflect` and `/calendar`.
- Admin routes require authenticated `ADMIN` role.

## 3. Primary User Flows

### A. New User

1. Visit `/`.
2. Click `Start reflecting`.
3. Go to `/signup` and create account.
4. Land on `/onboarding`.
5. Submit preferences.
6. Go to `/reflect`.
7. Start first guided session.

### B. Returning User

1. Visit `/login`.
2. Authenticate.
3. Resume open session card on `/reflect` or start a new category path.

### C. Reflection Session Flow

1. User opens `/reflect/:categoryName`.
2. Session starts or resumes.
3. Show prompt + timer + ambient controls.
4. Timer complete or skip accepted.
5. User can refresh (quota checked).
6. Repeat until closure threshold.
7. End session with final emotion + optional takeaway.
8. Save summary and navigate to session completion state.

### D. Calendar Flow

1. User opens `/calendar`.
2. Month view shows reflected markers.
3. User selects day.
4. If day is allowed by plan, open detail panel.
5. If locked, show premium gate treatment and upgrade CTA.

## 4. Plan-Gating by Experience

Free user behavior:

- refresh action limited daily
- session starts/day limited
- calendar details available only for today and previous day
- premium category attempts show upgrade messaging

Pro user behavior:

- expanded refresh and session limits
- full month day-detail access
- premium categories available
- richer history visibility

## 5. Session State Machine

States:

- `NOT_STARTED`
- `IN_PROGRESS`
- `PAUSED`
- `AWAITING_CLOSURE`
- `COMPLETED`
- `ENDED_EARLY`
- `SAFETY_INTERRUPTED`

Transitions:

- start -> `IN_PROGRESS`
- pause -> `PAUSED`
- resume -> `IN_PROGRESS`
- question budget exhausted / readiness met -> `AWAITING_CLOSURE`
- closure submit -> `COMPLETED`
- end-session action -> `ENDED_EARLY`
- danger-language detection -> `SAFETY_INTERRUPTED`

## 6. Navigation Standards

- Keep top-level nav minimal: Home, Reflect, Calendar.
- During active session, reduce navigation noise and keep an explicit `End session` control.
- On completion, provide clear path to `/calendar` and `/reflect`.
