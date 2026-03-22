# I Reflect Backend Architecture (MongoDB Target)

## 1. Backend Stack

- Java 21
- Spring Boot
- Spring Security + JWT
- Spring Data MongoDB
- Maven

Target architecture in MVP is Mongo-first. Existing JPA/Postgres code is treated as migration scaffolding.

## 2. Core Domain Enums

- `Role`: `USER`, `ADMIN`
- `Plan`: `FREE`, `PRO`
- `SubscriptionStatus`: `FREE`, `PRO_ACTIVE`, `PRO_PAST_DUE`, `PRO_CANCELED`, `PRO_EXPIRED`
- `SessionStatus`: `IN_PROGRESS`, `COMPLETED`, `ENDED_EARLY`, `SAFETY_INTERRUPTED`
- `PromptStage`: `OPENING`, `DEEPENING`, `REFRAMING`, `RELEASE`, `CLOSURE`
- `FinalEmotion`: `LIGHTER`, `RELIEVED`, `CALM`, `STILL_PROCESSING`, `HOPEFUL`

## 3. Collections

### `users`

Fields:

- `_id`
- `username`
- `email` (nullable)
- `passwordHash`
- `role`
- `plan`
- `subscriptionStatus`
- `createdAt`
- `updatedAt`

Indexes:

- unique: `username`
- unique sparse: `email`
- index: `plan`

### `userProfiles`

Fields:

- `_id`
- `userId`
- `displayName`
- `ageOrAgeRange`
- `gender`
- `statusInLife`
- `preferredTone`
- `currentMood`
- `primaryInterestCategory`
- `topicsHelpWith[]`
- `topicsToAvoid[]`
- `optionalContext`
- `createdAt`
- `updatedAt`

Indexes:

- unique: `userId`

### `categories`

Fields:

- `_id`
- `slug`
- `name`
- `description`
- `isPremium`
- `isActive`
- `promptStages`

`promptStages` shape:

- `opening[]`
- `deepening[]`
- `reframing[]`
- `release[]`
- `closure[]`

Indexes:

- unique: `slug`
- index: `isActive`
- index: `isPremium`

### `reflectionSessions`

Fields:

- `_id`
- `userId`
- `categorySlug`
- `status`
- `startedAt`
- `endedAt`
- `totalQuestions`
- `refreshesUsed`
- `finalCheckin`
- `finalEmotion`
- `summaryText`
- `prompts[]`
- `responses[]`
- `sessionTone`
- `inferredThemes[]`
- `completionReadinessScore`

Prompt object:

- `stepNumber`
- `promptText`
- `promptStage`
- `sourceType` (`CURATED`)
- `shownAt`

Response object:

- `promptStepNumber`
- `responseType`
- `responseText`
- `createdAt`

Indexes:

- compound: `{ userId: 1, startedAt: -1 }`
- index: `status`
- index: `categorySlug`

### `dailyUsage`

Fields:

- `_id`
- `userId`
- `usageDate` (`YYYY-MM-DD` in user timezone)
- `refreshesUsed`
- `sessionsStarted`

Indexes:

- unique compound: `{ userId: 1, usageDate: 1 }`

### `subscriptionEvents`

Fields:

- `_id`
- `userId`
- `provider` (`STRIPE`)
- `providerCustomerId`
- `providerSubscriptionId`
- `status`
- `payloadJson`
- `createdAt`

Indexes:

- index: `userId`
- index: `providerSubscriptionId`
- index: `createdAt`

## 4. Entitlement Service

Centralize plan checks in `EntitlementService` to avoid frontend-only gating.

Entitlements:

- `canUsePremiumCategories`
- `canRefreshQuestionUnlimited`
- `canViewFullHistory`
- `canUsePremiumCalendar`
- `canStartUnlimitedSessions`
- `canExportHistory`

Free limits are config-driven and enforced via backend checks against `dailyUsage`.

## 5. Auth and Security

- password hashing: BCrypt (Argon2 optional later)
- JWT access token with expiry
- refresh token flow optional for MVP if session duration requires it
- role/plan/entitlement checks at controller/service boundaries
- CORS restricted to frontend origins
- input validation on all request DTOs
- no sensitive reflection text in debug logs

## 6. Session Orchestration Service

`SessionService` responsibilities:

- start or resume sessions
- pick next prompt stage from session progress
- enforce refresh/session quotas by plan
- track completion readiness
- transition to closure stage
- finalize and persist summary data

## 7. Migration Notes from Current Repo

Current backend includes JPA/Postgres settings and entity/repository scaffolding.

Migration tasks:

- remove JPA dependencies and Postgres-only configuration from active runtime path
- add Spring Data MongoDB dependency and profile-based mongo config
- replace relational entities/repositories with Mongo document + repository classes
- retain existing route names where possible, move to `/api/*` namespace

## 8. Configuration

Use environment variables for:

- `MONGODB_URI`
- `JWT_SECRET`
- `JWT_EXP_MINUTES`
- `STRIPE_SECRET_KEY`
- `STRIPE_WEBHOOK_SECRET`
- `FRONTEND_ORIGIN`

No secrets in committed `application.properties`.
