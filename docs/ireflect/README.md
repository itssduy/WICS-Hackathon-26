# I Reflect Implementation Pack

This folder is the build-ready documentation set for the **I Reflect MVP**.

## What This Pack Is For

Use this pack to implement the product end-to-end during hackathon execution without re-deciding core product or architecture choices.

Locked defaults in this pack:

- datastore target: **MongoDB**
- question engine: **curated prompt banks + deterministic branching** (AI optional for end summary only)
- monetization: **Free + Pro ($20/month)**
- core visual language: **warm sunrise into deep reflective blue** with soft premium surfaces

## Reading Order

1. `01-product-spec.md`
2. `02-design-system.md`
3. `03-app-flow-routes.md`
4. `04-backend-architecture-mongo.md`
5. `05-api-contracts.md`
6. `06-question-engine-safety.md`
7. `07-implementation-phases.md`
8. `08-test-matrix.md`

## Current Repo Notes

This repo currently contains starter React + Spring Boot code and JPA/Postgres scaffolding. The docs in this folder define the target MVP architecture and behavior.

Implementation should treat JPA/Postgres pieces as temporary scaffolding and move to Mongo-backed services for MVP feature work.
