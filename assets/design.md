# Design System Strategy: Radiant Reflection

## 1. Overview & Creative North Star

This design system is anchored in the **"Ethereal Dawn"** Creative North Star. It is a visual metaphor for the transition from the quiet introspection of night to the vibrant clarity of morning. Unlike standard wellness apps that rely on clinical white space, this system embraces **tonal depth and organic layering**.

We move beyond the "template" look by utilizing intentional asymmetry—mimicking the natural silhouette of the sunrise landscape—and a high-contrast typography scale that feels editorial and premium. Surfaces are not mere containers; they are layers of light and atmosphere that overlap and breathe, creating an emotionally intelligent environment for self-reflection.

---

## 2. Colors: The Atmosphere of Mindfulness

The palette is a direct extraction from the warm radiance of the sun and the grounding shadows of the earth.

### Core Palette
*   **Primary (`primary` #8f4900 / `primary_container` #fe9740):** The glowing amber of the rising sun. Use these for primary actions and moments of "enlightenment" in the UI.
*   **Secondary (`secondary` #4d5b7d):** The tranquil navy of the pre-dawn sky. This provides the grounding, "midnight blue" stability for deep-focus areas.
*   **Tertiary (`tertiary` #814e47):** The muted dusk mauve and softened brown. Use this for earthiness and human warmth.
*   **Surface Hierarchy:** Our backgrounds transition from `surface_container_lowest` (#ffffff) to `surface_dim` (#fec8bc) to mimic the shifting light of a sunrise.

### The "No-Line" Rule
**Borders are strictly prohibited for sectioning.** To define boundaries, designers must use background color shifts or tonal transitions. For example, a `surface_container_low` card should sit on a `surface` background. The eye should perceive depth through color, not structural lines.

### Signature Textures & Glassmorphism
To provide "visual soul," apply subtle gradients transitioning from `primary` to `primary_container` for hero moments. For floating elements, such as the signup card or reflection prompts, use **Glassmorphism**:
*   **Background:** `surface` at 60% opacity.
*   **Effect:** Backdrop-blur (12px–20px).
*   **Glow:** An inner shadow using `primary_fixed` at 15% opacity to simulate a warm ambient light hitting the edge of the glass.

---

## 3. Typography: Editorial Clarity

The typography uses a humanist pairing to balance premium elegance with approachable warmth.

*   **Headings (Plus Jakarta Sans):** Selected for its modern, rounded, and humanist feel. 
    *   *Role:* `display-lg` and `headline-md` are used for high-impact reflection prompts and daily greetings. The generous letter spacing conveys a sense of "airiness."
*   **Body (Manrope):** A highly readable, relaxed sans-serif.
    *   *Role:* `body-lg` is the workhorse for journaling and long-form reading. It is functional yet retains the "Modern Wellness" aesthetic through its open counters.
*   **Labels (Manrope):** Used at smaller scales (`label-md`) for secondary metadata, ensuring the UI remains uncluttered and quiet.

---

## 4. Elevation & Depth: Tonal Layering

We reject traditional drop shadows in favor of **Ambient Tonal Layering**.

*   **The Layering Principle:** Stacking surface tiers creates a natural lift. A `surface_container_high` (#ffdad2) element placed on a `surface` (#fff4f2) background creates enough contrast to signify hierarchy without a single pixel of shadow.
*   **Ambient Shadows:** If a "floating" effect is mandatory, use an extra-diffused shadow. 
    *   *Spec:* `0px 20px 40px rgba(65, 41, 35, 0.06)`. The shadow color is a tint of the brown `on_surface` color, not grey.
*   **The Ghost Border:** If a boundary is needed for accessibility, use the `outline_variant` (#caa59c) at **15% opacity**. High-contrast, 100% opaque borders are forbidden.

---

## 5. Components

### Buttons
*   **Primary:** Uses the golden `primary_container` (#fe9740) with `on_primary_container` (#4f2600) text. Roundedness: `full`.
*   **Secondary:** A glassmorphic effect with a `ghost-border` (15% opacity `outline`).
*   **States:** Hover states should involve a "glow" increase (increasing the ambient shadow spread) rather than a simple color darken.

### Cards & Lists
*   **The "No-Divider" Rule:** Vertical white space (specifically `spacing.8` or `spacing.10`) must be used to separate list items. 
*   **Styling:** Use `rounded-2xl` (1.5rem) or `rounded-xl` (3rem) for all cards to maintain the "soft" aesthetic.

### Input Fields
*   **Style:** Minimalist. Instead of a 4-sided box, use a `surface_container` background with a subtle bottom-heavy `primary` glow when focused.
*   **Labels:** Use `title-sm` to give labels a more intentional, editorial feel than standard small caption text.

### Reflection-Specific Components
*   **The "Radiance" Progress Bar:** A gradient bar transitioning from `secondary` (tranquil navy) to `primary` (sunrise amber), symbolizing the journey from darkness to light.
*   **Insight Chips:** Small, pill-shaped `tertiary_container` elements for tagging moods or reflection themes.

---

## 6. Do’s and Don’ts

### Do:
*   **Do** use asymmetrical layouts. Place the "Meditating Silhouette" (logo) or "Sunrise Tree" as organic background elements that bleed off the edge of the screen.
*   **Do** prioritize "Breathing Room." Use the `24` (8.5rem) spacing token for hero margins to create a sense of calm.
*   **Do** use the `light emerging from darkness` theme vertically: Navy/Secondary in the footer, transitioning to Amber/Primary at the top.

### Don't:
*   **Don't** use pure black (#000000). Use `inverse_surface` (#1c0905) for the deepest tones to maintain a warm, organic feel.
*   **Don't** use sharp corners. The minimum radius for any container is `md` (1.5rem).
*   **Don't** use standard 1px grey dividers. If you must separate content, use a background color shift or 1.4rem of empty space.