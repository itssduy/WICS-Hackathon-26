# Hackathon Implementation Phases

## Phase 0: Foundation Alignment (Half Day)

- wire route shell for all target pages
- establish shared theme tokens and typography
- define core backend enums and DTO contracts
- add Mongo connection profile and env wiring

Deliverable:

- app boots with route skeletons and API stubs matching contracts

## Phase 1: Core Journey (Day 1)

Frontend:

- landing page with mission, category showcase, and pricing
- signup/login split layouts matching brand direction
- onboarding form with chip-based preference selectors
- `/reflect` dashboard with category cards and continue-session card

Backend:

- auth signup/login endpoints with hashed passwords and JWT
- profile create/get/update endpoints
- categories read endpoints from seeded data

Deliverable:

- user can authenticate and reach reflect dashboard with category options

## Phase 2: Reflection Engine (Day 2)

Frontend:

- `/reflect/:categoryName` immersive session screen
- cloud question bubble, timer presets/custom, ambient controls
- progress indicator and session controls (refresh, pause, end)
- closure check-in modal/state

Backend:

- session start/get/next/refresh/end endpoints
- prompt-stage progression service
- daily usage tracking and free-limit enforcement
- summary generation fallback

Deliverable:

- complete guided session from first question through saved completion

## Phase 3: Calendar, Streak, and Gating (Day 3)

Frontend:

- calendar view with reflection markers and day detail panel
- streak/stat cards
- locked day UI states for free plan
- upgrade prompts on locked interactions

Backend:

- month/day calendar endpoints
- streak endpoint
- entitlement checks for calendar access window

Deliverable:

- user can revisit eligible days and see plan-based lock behavior

## Phase 4: Billing and Admin (Day 4)

Frontend:

- billing page and upgrade CTA flow
- admin category/prompt management UI (basic)

Backend:

- Stripe checkout-session endpoint
- Stripe webhook processing for subscription updates
- billing status endpoint
- admin endpoints for categories and prompt management

Deliverable:

- plan status can be upgraded and reflected in entitlements

## Phase 5: Hardening and Demo Polish (Day 5)

- safety interruption flow validation
- authentication and entitlement test pass
- content QA for copy tone and design consistency
- accessibility and responsive fixes
- demo script preparation with seeded demo accounts

Deliverable:

- stable MVP demo with complete primary flows

## Cross-Cutting Rules During Build

- Do not bypass backend entitlement checks.
- Keep all behavior aligned to `05-api-contracts.md`.
- Keep visual implementation aligned to `02-design-system.md`.
- Prioritize completion of primary user journey over feature expansion.
