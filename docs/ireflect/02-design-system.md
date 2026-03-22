# I Reflect Design System (MVP)

This document codifies the visual language from:

- `/Users/pete/Downloads/stitch/DESIGN.md`
- `/Users/pete/Downloads/stitch/code.html`
- provided logo/background/page mock assets

## 1. Creative Direction

Theme: **Ethereal Dawn**

- upper field: hopeful warm radiance
- lower field: deep reflective calm
- emotional intent: safe, intimate, premium, quiet

Visual tone should avoid default SaaS and avoid clinical therapy UI conventions.

## 2. Color System

Primary palette:

- `primary`: `#8F4900`
- `primaryContainer`: `#FE9740`
- `secondary`: `#4D5B7D`
- `tertiary`: `#814E47`
- `surface`: `#FFF4F2`
- `surfaceDim`: `#FEC8BC`
- `inverseSurface`: `#1C0905`

Brand atmosphere colors:

- warm amber glow: `#F59E0B`
- deep navy anchor: `#1E3A8A`

Rules:

- no harsh black and white contrast blocks
- no neon accents
- no heavy dark “doom” mode treatment
- no visible hard divider lines for sectioning

## 3. Typography

- Headline font: **Plus Jakarta Sans**
- Body font: **Manrope**

Scale intent:

- hero headlines: bold, large, editorial presence
- body: relaxed, readable, calm pacing
- labels: compact and clean

## 4. Surfaces and Depth

Surface strategy:

- depth through tonal layering and blur, not hard borders
- cards use large radii (min 16px, common 24px to full pill)
- primary floating cards use soft glassmorphism

Glass card baseline:

- background: warm surface color at 60%-80% opacity
- blur: 12px-24px
- shadow: diffused warm shadow (`rgba(65, 41, 35, 0.06)` to `0.12`)
- optional inner edge glow in amber at low alpha

## 5. Motion and Interaction

- question entry: fade + upward float + soft bloom
- timer completion: gentle dissolve transition
- hover: brightness and glow shifts, no jumpy transforms
- ambient particles/mist: subtle and slow

Avoid:

- aggressive spring motion
- frequent bouncing elements
- flashy loading effects

## 6. Core Components

### Buttons

Primary CTA:

- pill shape
- warm amber gradient fill
- strong contrast text in dark warm brown
- soft ambient glow

Secondary button:

- translucent/glass style
- light outline at very low opacity

### Inputs

- rounded, soft container input fields
- muted placeholder text
- focus ring in warm primary tone
- optional bottom glow on focus

### Category Cards

Content per card:

- category title
- short emotional framing
- 2-3 prompt examples
- CTA (“Reflect here”, “Enter this space”, etc.)

Visual:

- premium image or gradient panel
- rounded corners
- subtle highlight and depth

### Question Cloud Bubble

- cloud-like silhouette
- translucent soft white with glow
- centered elegant text
- prominent but calm visual anchor

### Calendar Day States

- reflected day: glowing dot/ring
- selected day: elevated warm highlight
- locked day (free plan): blurred/faded treatment with lock indicator

## 7. Page-Specific Styling Notes

### Landing

- hero with warm sunrise image and deep-blue grounding
- alternating category showcase blocks
- premium upgrade section with `$20/month`

### Signup / Login

- split-screen layout
- left: immersive branded panel
- right: elevated auth card with glass + warm neutral surface

### Reflect Dashboard

- category-first layout as emotional entry point
- continue-session card below category grid
- minimal stats and calendar shortcut

### Reflection Session

- cinematic full-height stage
- progress indicator + category context
- cloud question bubble + timer cluster + ambient controls

### Calendar

- large premium calendar container
- day-detail panel/modal
- upgrade prompt integrated tastefully

## 8. Responsive Behavior

- desktop: split layouts and spacious cards
- tablet/mobile: stack sections vertically
- preserve key atmosphere (gradient, glow, rounded surfaces) at all breakpoints
- keep CTA hierarchy obvious on small screens

## 9. Asset Usage Guidance

- Use provided meditating silhouette as primary logo mark.
- Use provided sunrise/deep-blue scene as core hero and auth background direction.
- Keep image treatments warm and softly diffused to preserve emotional consistency.
