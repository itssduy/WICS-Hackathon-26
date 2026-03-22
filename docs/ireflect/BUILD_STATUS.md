# Build Status

## Current Phase

- Phase: 5 — Demo Polish 🚧 IN PROGRESS
- Last updated: 2026-03-21T19:56:00-05:00

## Completed

### Phase 0: Foundation Alignment

**Frontend (Vite + React)**
- [x] Design system implemented (`index.css`) — Ethereal Dawn theme tokens, glass cards, buttons, inputs, animations
- [x] 15 route skeletons wired in `main.jsx` via React Router
- [x] `AuthContext` — login, signup, logout, JWT token management
- [x] `ProtectedRoute` — guards for authenticated + admin routes
- [x] `Navbar` — glass nav bar, conditional links, hidden during sessions
- [x] `AppShell` — layout wrapper with optional navbar
- [x] Landing page — hero, features grid, 4 category showcases, pricing, footer
- [x] Login page — split layout, branded panel, glass form card, Google button
- [x] Signup page — split layout, form with validation, Google button
- [x] Onboarding page — multi-step (name, mood/tone, topics) with chip selectors
- [x] Reflect Dashboard — category card grid with gradients and sample prompts
- [x] Safety/Privacy/Terms — content pages with crisis resources
- [x] Skeleton pages created: ReflectSession, Calendar, History, Profile, Billing, AdminCategories, AdminPrompts
- [x] `index.html` SEO — meta description, theme-color, favicon, title
- [x] `.env` — `VITE_API_URL=http://localhost:8080/api`
- [x] Assets copied: `logo.png`, `background.png` → `frontend/public/`
- [x] Build passes: 45 modules, 0 errors

**Backend (Spring Boot 4 + MongoDB)**
- [x] `pom.xml` migrated: removed JPA/Postgres/Flyway → added Spring Data MongoDB, JJWT, Validation
- [x] Old `com.example.demo` package removed, new `com.ireflect` package created
- [x] Domain enums: `Role`, `Plan`, `SubscriptionStatus`, `SessionStatus`, `PromptStage`, `FinalEmotion`
- [x] MongoDB documents: `UserDocument`, `UserProfileDocument`, `CategoryDocument` (with nested `PromptStages`/`PromptItem`)
- [x] Repositories: `UserRepository`, `UserProfileRepository`, `CategoryRepository`
- [x] DTOs: `SignupRequest`, `LoginRequest`, `AuthResponse`, `ApiError`, `CategoryResponse`
- [x] `JwtService` — token generation/parsing with JJWT (HS256)
- [x] `JwtAuthFilter` — Bearer token extraction → SecurityContext
- [x] `SecurityConfig` — stateless JWT, CORS, BCrypt, route-level access control
- [x] `AuthController` — `POST /api/auth/signup`, `POST /api/auth/login`
- [x] `CategoryController` — `GET /api/categories`, `GET /api/categories/available`
- [x] `ProfileController` — `GET /api/profile`, `POST /api/profile`, `PUT /api/profile`
- [x] `DataSeeder` — seeds 4 launch categories with prompts across 5 stages on first boot
- [x] `application.properties` — MongoDB URI, JWT config, CORS, plan limits
- [x] Compile passes: 0 errors

### Phase 1: Core Journey

- [x] ReflectDashboard fetches categories from API (with fallback to hardcoded data)
- [x] Personalized greeting with username
- [x] Continue-session card for active in-progress sessions
- [x] Loading state with animated dots
- [x] All Phase 0 frontend pages already satisfied Phase 1's landing/signup/login/onboarding requirements

### Phase 2: Reflection Engine

**Frontend**
- [x] `/reflect/:categoryName` immersive session screen
- [x] Dark ambient background with glowing prompt cloud
- [x] Timer presets (3, 5, 10 min) with pause/resume
- [x] Progress bar and stage badge (opening, deepening, reframing, release, closure)
- [x] Optional text response textarea
- [x] Refresh question button (calls API)
- [x] Next question button (calls API, records response)
- [x] End session button with closure check-in modal
- [x] Emotion picker (lighter, relieved, calm, still processing, hopeful)
- [x] Final check-in and takeaway text inputs
- [x] Completion summary screen
- [x] Safety interruption screen with crisis resources

**Backend**
- [x] `SessionDocument` — MongoDB document with prompt/response history, usage tracking, closure fields
- [x] `SessionRepository` — queries for active, daily, and latest sessions
- [x] `SafetyService` — danger-language detection with pattern matching, supportive messaging, crisis resources
- [x] `SessionOrchestrationService` — full reflection engine:
  - Session start with daily limit enforcement (3 sessions/day free)
  - 5-stage prompt progression (opening → deepening → reframing → release → closure)
  - Prompt selection with topic avoidance filtering
  - Refresh with daily quota tracking (5/day free)
  - Safety interruption on danger language in responses
  - Template fallback summary generation
- [x] `SessionController` — all API endpoints:
  - `POST /api/sessions/start`
  - `GET /api/sessions/{id}`
  - `POST /api/sessions/{id}/next-question`
  - `POST /api/sessions/{id}/refresh-question`
  - `POST /api/sessions/{id}/end`
  - `GET /api/sessions/open`
  - `GET /api/sessions/latest`
- [x] Frontend build passes: 46 modules, 0 errors
- [x] Backend compile passes: 0 errors

### Phase 3: Calendar, Streak, and Gating

**Frontend**
- [x] Calendar page — month grid with session markers (dots), day detail panel, month navigation
- [x] Streak sidebar — current streak, longest streak, this week, this month
- [x] Locked day UI states for free plan (ready, gating logic in backend)
- [x] History page — session cards with category, date, summary, emotion, prompt count
- [x] Profile page — account info, editable preferences (display name, tone, mood), chip selectors

**Backend**
- [x] `CalendarController` — `GET /api/calendar/month`, `GET /api/calendar/day`, `GET /api/streak`
- [x] `SessionController` — added `GET /api/sessions/history`
- [x] Frontend build passes: 49 modules, 0 errors
- [x] Backend compile passes: 0 errors

### Phase 4: Billing and Admin

**Frontend**
- [x] Billing page — current plan card, Pro upgrade card with feature list and pricing
- [x] Stripe checkout integration button (connected to backend)
- [x] Active plan state display

**Backend**
- [x] `BillingController` — `POST /api/billing/create-checkout-session`, `GET /api/billing/status`, `POST /api/billing/webhook`
- [x] `AdminController` — `POST /api/admin/categories`, `PUT /api/admin/categories/{id}`, `GET /api/admin/categories`
- [x] `SecurityConfig` — billing webhook permitted without auth
- [x] Frontend build passes: 50 modules, 0 errors
- [x] Backend compile passes: 0 errors

### Phase 5: Demo Polish (In Progress)

- [x] Frontend lint clean (`npm run lint`)
- [x] Frontend production build verified (`npm run build`)
- [x] Auth context hardened to avoid effect-driven state churn and token decode regressions
- [x] Removed unused state/variables in `Calendar`, `Profile`, and `ReflectSession` pages
- [x] Added backend safety unit tests (`SafetyServiceTest`)
- [x] Backend tests pass (`./mvnw test`) with 3 passing tests
- [x] Created demo walkthrough doc: `docs/ireflect/DEMO_WALKTHROUGH.md`
- [x] Playwright E2E pass completed:
  - Landing -> Signup (random user) -> Onboarding -> Reflect Dashboard
  - Reflection session timer + pause/resume + next + refresh + end
  - Calendar streak and same-day session detail verification
- [x] Fixed nav discoverability: Reflect/Calendar links visible for all users
- [x] Fixed auth UX: login returns to originally requested protected route
- [x] Improved auth error message for backend connectivity failures (replaces vague `Failed to fetch`)

## In Progress

- Phase 5 remaining checks:
  - Manual responsive sweep (desktop/tablet/mobile) for landing, auth, reflect, session, calendar
  - Manual Stripe flow verification once keys are available

## Next

- Execute remaining manual Phase 5 checks and mark Phase 5 complete.

## Blockers

- Backend requires a running MongoDB instance (`mongodb://localhost:27017/ireflect`)
- Stripe integration requires API keys (currently placeholder)
