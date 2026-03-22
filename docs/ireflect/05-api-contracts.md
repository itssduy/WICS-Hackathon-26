# I Reflect API Contracts (MVP)

Base path: `/api`

Auth: JWT bearer for authenticated endpoints.

## 1. Error Model

All non-2xx responses return:

```json
{
  "timestamp": "2026-03-21T18:00:00Z",
  "status": 403,
  "code": "PLAN_LIMIT_REACHED",
  "message": "Daily refresh limit reached for free plan",
  "path": "/api/sessions/{id}/refresh-question"
}
```

Common codes:

- `VALIDATION_ERROR`
- `UNAUTHORIZED`
- `FORBIDDEN`
- `PLAN_LIMIT_REACHED`
- `CATEGORY_LOCKED`
- `SESSION_NOT_FOUND`
- `SAFETY_INTERRUPT`

## 2. Auth

### POST `/api/auth/signup`

Request:

```json
{
  "username": "maya",
  "password": "strong-password",
  "confirmPassword": "strong-password"
}
```

Response:

```json
{
  "userId": "u_123",
  "username": "maya",
  "plan": "FREE",
  "subscriptionStatus": "FREE",
  "token": "jwt-access-token"
}
```

### POST `/api/auth/login`

Request:

```json
{
  "usernameOrEmail": "maya",
  "password": "strong-password"
}
```

Response same shape as signup.

### POST `/api/auth/refresh`

Request:

```json
{
  "refreshToken": "refresh-token"
}
```

Response:

```json
{
  "token": "new-access-token"
}
```

## 3. Profile

### GET `/api/profile`

Response:

```json
{
  "displayName": "Maya",
  "ageOrAgeRange": "18-24",
  "gender": "Prefer not to say",
  "statusInLife": "Student",
  "preferredTone": "gentle",
  "currentMood": "calm",
  "primaryInterestCategory": "deep-meaning-thoughts",
  "topicsHelpWith": ["stress", "purpose"],
  "topicsToAvoid": ["family"]
}
```

### POST `/api/profile`

Create profile with same payload fields as GET response.

### PUT `/api/profile`

Partial/full update of profile fields.

## 4. Categories

### GET `/api/categories`

Returns all active categories for UI rendering:

```json
[
  {
    "slug": "deep-meaning-thoughts",
    "name": "Deep meaning thoughts",
    "description": "A grounded space for purpose and inner truth.",
    "isPremium": false,
    "samplePrompts": [
      "What truth have you been quietly carrying?",
      "What part of yourself have you been neglecting lately?"
    ]
  }
]
```

### GET `/api/categories/available`

Same shape as `/api/categories`, filtered by entitlement.

## 5. Sessions

### POST `/api/sessions/start`

Request:

```json
{
  "categorySlug": "deep-meaning-thoughts"
}
```

Response:

```json
{
  "sessionId": "s_123",
  "status": "IN_PROGRESS",
  "categorySlug": "deep-meaning-thoughts",
  "questionNumber": 1,
  "questionTotalHint": 5,
  "prompt": {
    "stepNumber": 1,
    "promptStage": "OPENING",
    "promptText": "What truth have you been quietly carrying?"
  },
  "dailyUsage": {
    "refreshesUsed": 0,
    "refreshesLimit": 5,
    "sessionsStarted": 1,
    "sessionsLimit": 3
  }
}
```

### GET `/api/sessions/{id}`

Returns full session snapshot (status, prompt history, response history, and progress).

### POST `/api/sessions/{id}/next-question`

Request:

```json
{
  "responseType": "REFLECTION_NOTE",
  "responseText": "optional"
}
```

Response:

```json
{
  "sessionId": "s_123",
  "questionNumber": 2,
  "questionTotalHint": 5,
  "prompt": {
    "stepNumber": 2,
    "promptStage": "DEEPENING",
    "promptText": "What kind of life feels most honest to you?"
  },
  "status": "IN_PROGRESS"
}
```

### POST `/api/sessions/{id}/refresh-question`

Request body optional.

Response:

```json
{
  "sessionId": "s_123",
  "prompt": {
    "stepNumber": 2,
    "promptStage": "DEEPENING",
    "promptText": "What belief is making this heavier than it needs to be?"
  },
  "dailyUsage": {
    "refreshesUsed": 2,
    "refreshesLimit": 5
  }
}
```

### POST `/api/sessions/{id}/end`

Request:

```json
{
  "finalEmotion": "LIGHTER",
  "finalCheckin": "I feel clearer now",
  "takeaway": "I want to be more honest with myself"
}
```

Response:

```json
{
  "sessionId": "s_123",
  "status": "COMPLETED",
  "summaryText": "You explored identity and pressure, and ended feeling lighter with a clearer intention.",
  "endedAt": "2026-03-21T18:10:00Z"
}
```

### GET `/api/sessions/open`

Response:

```json
{
  "hasOpenSession": true,
  "session": {
    "sessionId": "s_123",
    "categorySlug": "exciting-thoughts",
    "questionNumber": 3,
    "lastActiveAt": "2026-03-21T17:58:00Z",
    "previewPrompt": "What future version of you feels thrilling to imagine?"
  }
}
```

### GET `/api/sessions/history`

Query params:

- `page`
- `size`
- optional `categorySlug`

Response contains paginated session summaries.

## 6. Session Config

### GET `/api/session-config/timer-options`

```json
{
  "presets": [3, 5, 10],
  "customEnabled": true,
  "customMin": 1,
  "customMax": 30
}
```

### GET `/api/session-config/ambient-audio`

```json
{
  "tracks": [
    { "id": "drift", "label": "Drift", "url": "/audio/drift.mp3" }
  ]
}
```

## 7. Calendar

### GET `/api/calendar/month?year=YYYY&month=MM`

Response:

```json
{
  "year": 2026,
  "month": 3,
  "plan": "FREE",
  "days": [
    {
      "date": "2026-03-21",
      "hasSession": true,
      "sessionCount": 1,
      "isAccessible": true,
      "isLocked": false
    }
  ]
}
```

### GET `/api/calendar/day?date=YYYY-MM-DD`

Response:

```json
{
  "date": "2026-03-21",
  "isAccessible": true,
  "entries": [
    {
      "sessionId": "s_123",
      "category": "Purely happy thoughts",
      "summaryText": "You focused on gratitude and warmth.",
      "finalEmotion": "CALM",
      "themes": ["gratitude", "peace"]
    }
  ]
}
```

### GET `/api/streak`

```json
{
  "currentStreak": 6,
  "longestStreak": 14,
  "reflectionsThisWeek": 5,
  "reflectionsThisMonth": 14
}
```

## 8. Billing

### POST `/api/billing/create-checkout-session`

Response:

```json
{
  "checkoutUrl": "https://checkout.stripe.com/..."
}
```

### POST `/api/billing/webhook`

Stripe webhook endpoint. Verifies signature and updates subscription status.

### GET `/api/billing/status`

```json
{
  "plan": "PRO",
  "subscriptionStatus": "PRO_ACTIVE",
  "renewalDate": "2026-04-21"
}
```

## 9. Admin

### POST `/api/admin/categories`

Create category including stage prompt arrays.

### PUT `/api/admin/categories/{id}`

Update category metadata and prompt stages.

### POST `/api/admin/prompts`

Add prompt entries to existing category stages.
