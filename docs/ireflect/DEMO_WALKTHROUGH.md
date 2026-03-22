# I Reflect Demo Walkthrough

Use this script for hackathon demos and final verification.

## 1. Demo Setup

- Start MongoDB at `mongodb://localhost:27017/ireflect`
- Start backend: `cd backend && ./mvnw spring-boot:run`
- Start frontend: `cd frontend && npm run dev`
- Open app at `http://localhost:5173`

## 2. Primary Demo Flow (6-8 minutes)

1. **Landing experience**
   - Show hero, categories, and Pro pricing section.
   - Highlight calm visual identity and emotional positioning.

2. **Signup and onboarding**
   - Create account from signup page.
   - Complete onboarding chips for mood/tone/topics.

3. **Reflect dashboard**
   - Show four launch categories.
   - Open a category session.

4. **Guided reflection session**
   - Show cloud prompt bubble and timer presets.
   - Start timer, submit optional note, move to next question.
   - Demonstrate refresh question.
   - End with closure check-in and takeaway.

5. **Completion and history**
   - Show session summary.
   - Navigate to calendar and open reflected day detail.
   - Show streak card.

6. **Billing and plan gate**
   - Open billing page.
   - Show Pro value and upgrade action.

## 3. Safety Flow Demo (1-2 minutes)

In a reflection response, enter danger language such as:

- `I feel like I want to die`

Expected behavior:

- session is interrupted
- supportive message appears
- crisis resources are displayed
- user can exit to Safety resources

## 4. Admin Demo (Optional)

- Use admin account to open category management.
- Show create/update category prompt capabilities.

## 5. Known Demo Constraints

- Stripe checkout requires valid API keys.
- Calendar detail gating depends on plan and available seeded session data.
- Some manual responsive checks should be performed before judging final polish complete.
