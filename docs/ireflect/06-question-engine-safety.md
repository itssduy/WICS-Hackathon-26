# Question Engine and Safety Spec (MVP)

## 1. Engine Mode

Locked MVP mode:

- curated prompt banks per category
- deterministic session progression logic
- no per-turn freeform LLM generation
- optional AI summarization only at session end

## 2. Prompt Bank Structure

Each category stores staged prompts:

- `opening`
- `deepening`
- `reframing`
- `release`
- `closure`

Prompt item fields:

- `id`
- `text`
- `tone` (gentle/deep/encouraging/calm/direct-kind)
- `sensitivityTags[]`
- `blockedTopics[]`

## 3. Progression Logic

Session length target: 4-7 prompts.

Selection policy:

1. stage starts at `opening`
2. advance toward `deepening`
3. allow one `reframing`
4. move to `release`
5. finalize at `closure`

Completion decision factors:

- prompt count reached minimum (4)
- user states relief/clarity
- completion readiness score crosses threshold
- hard max prompt count reached (7)

## 4. Refresh Logic

Refresh behavior:

- refresh replaces current prompt with another from same stage where possible
- avoid repeating prompt IDs within same session
- decrement daily refresh quota
- block when quota is exhausted

Free defaults:

- `dailyRefreshLimit = 5`
- `dailySessionStartLimit = 3`

## 5. Safety Rules

The reflection engine must:

- ask one question at a time
- avoid diagnosis language
- avoid coercion and guilt framing
- avoid telling user what they feel
- avoid escalating paranoia or shame
- close gently when depth is sufficient

## 6. Danger-Language Handling

Trigger sources:

- free-text response inputs
- optional takeaway text
- final check-in text

Flow on detection:

1. set session status to `SAFETY_INTERRUPTED`
2. stop question progression immediately
3. show supportive message
4. show emergency resources and crisis help route
5. provide `End session now` and `View resources` actions

## 7. Topic Avoidance and Personalization

During selection, filter prompts by:

- `topicsToAvoid` from onboarding
- category sensitivity tags
- blocked prompt topic tags

Tone adaptation:

- select prompts matching preferred tone if possible
- fallback to neutral gentle prompts when exact tone unavailable

## 8. Summary Generation (Optional AI)

Input set:

- category
- prompts shown
- final emotion/check-in
- optional takeaway

Output constraints:

- concise (2-4 sentences)
- non-judgmental
- no diagnosis or overclaiming

If AI unavailable, use template summary fallback.

## 9. Observability

Track metrics:

- session completion rate
- average prompts per session
- refresh use rate
- safety interruption count
- per-category completion and relief outcomes

Do not store sensitive raw text in verbose logs.
