# Continuous Build Runbook (Use This Every Session)

Use this file as the single source of truth when implementing I Reflect continuously.

## Required Context Files (Load In This Order)

1. `docs/ireflect/README.md`
2. `docs/ireflect/01-product-spec.md`
3. `docs/ireflect/02-design-system.md`
4. `docs/ireflect/03-app-flow-routes.md`
5. `docs/ireflect/04-backend-architecture-mongo.md`
6. `docs/ireflect/05-api-contracts.md`
7. `docs/ireflect/06-question-engine-safety.md`
8. `docs/ireflect/07-implementation-phases.md`
9. `docs/ireflect/08-test-matrix.md`

## Continuous Execution Rules

- Implement only what is specified in the docs above.
- Respect API contracts exactly as defined in `05-api-contracts.md`.
- Respect design direction and assets in `02-design-system.md` and `assets/`.
- Enforce free/pro and safety behavior server-side.
- After each coding step, run relevant checks/tests and summarize results.
- Do not stop at planning; complete working code slices end-to-end.

## Work Loop

1. Read required context files.
2. Pick the next unfinished item from `07-implementation-phases.md`.
3. Implement it fully (frontend + backend if required).
4. Run tests/checks.
5. Report what changed, what passed, and what is next.
6. Repeat until MVP acceptance criteria are met.

## Completion Gate

MVP is complete when:

- acceptance criteria in `01-product-spec.md` are satisfied
- key scenarios in `08-test-matrix.md` pass
- implementation matches `02-design-system.md` and `05-api-contracts.md`
