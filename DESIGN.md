---
name: Functional Crafting Utility
colors:
  surface: '#0b1326'
  surface-dim: '#0b1326'
  surface-bright: '#31394d'
  surface-container-lowest: '#060e20'
  surface-container-low: '#131b2e'
  surface-container: '#171f33'
  surface-container-high: '#222a3d'
  surface-container-highest: '#2d3449'
  on-surface: '#dae2fd'
  on-surface-variant: '#c2c6d6'
  inverse-surface: '#dae2fd'
  inverse-on-surface: '#283044'
  outline: '#8c909f'
  outline-variant: '#424754'
  surface-tint: '#adc6ff'
  primary: '#adc6ff'
  on-primary: '#002e6a'
  primary-container: '#4d8eff'
  on-primary-container: '#00285d'
  inverse-primary: '#005ac2'
  secondary: '#4edea3'
  on-secondary: '#003824'
  secondary-container: '#00a572'
  on-secondary-container: '#00311f'
  tertiary: '#ffb95f'
  on-tertiary: '#472a00'
  tertiary-container: '#ca8100'
  on-tertiary-container: '#3e2400'
  error: '#ffb4ab'
  on-error: '#690005'
  error-container: '#93000a'
  on-error-container: '#ffdad6'
  primary-fixed: '#d8e2ff'
  primary-fixed-dim: '#adc6ff'
  on-primary-fixed: '#001a42'
  on-primary-fixed-variant: '#004395'
  secondary-fixed: '#6ffbbe'
  secondary-fixed-dim: '#4edea3'
  on-secondary-fixed: '#002113'
  on-secondary-fixed-variant: '#005236'
  tertiary-fixed: '#ffddb8'
  tertiary-fixed-dim: '#ffb95f'
  on-tertiary-fixed: '#2a1700'
  on-tertiary-fixed-variant: '#653e00'
  background: '#0b1326'
  on-background: '#dae2fd'
  surface-variant: '#2d3449'
typography:
  display:
    fontFamily: Inter
    fontSize: 36px
    fontWeight: '700'
    lineHeight: '1.2'
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: '1.3'
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: '1.4'
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: '1.5'
  body-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: '1.5'
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '600'
    lineHeight: '1'
    letterSpacing: 0.05em
  mono-data:
    fontFamily: JetBrains Mono
    fontSize: 13px
    fontWeight: '500'
    lineHeight: '1'
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  base: 4px
  xs: 4px
  sm: 8px
  md: 16px
  lg: 24px
  xl: 32px
  gutter: 16px
  margin-mobile: 16px
  margin-desktop: 48px
---

## Brand & Style
The design system focuses on a **Utility-First** philosophy tailored for gaming optimization and crafting planning. The goal is to maximize information density without sacrificing clarity, moving away from typical "gamer" aesthetics (neon glows, heavy textures) toward a professional, data-driven environment.

The brand personality is **Precise, Reliable, and Efficient**. It targets "power users"—players who treat crafting and resource management as a meta-game. The UI evokes a sense of organized control, using a deep, nocturnal palette that minimizes eye strain during long gaming sessions while highlighting critical task completion states with vibrant, purposeful accents.

## Colors
This design system utilizes a high-contrast dark theme. The foundation is built on deep blue-greys (`Slate 900` and `Slate 800`) to create a professional, grounded atmosphere.

- **Primary Canvas:** Deep navy-grey provides the lowest visual energy base.
- **Surface Layers:** Lighter shades of navy-grey distinguish cards and interactive sections.
- **Accents:** High-chroma colors are used sparingly and exclusively for functional feedback. Green signifies "Craftable" or "Complete," Yellow for "In Progress," and Red for "Missing Materials."
- **Text:** Crisp white is reserved for high-priority data and headers, while muted grey-blue is used for labels and secondary metadata to manage visual hierarchy in data-dense views.

## Typography
The system uses **Inter** for all UI elements to ensure maximum legibility at small sizes, which is essential for data-heavy crafting tables.

**Key Principles:**
- **Hierarchy through Weight:** Use Bold/Semi-Bold for headers and Medium for interactive labels.
- **Uppercase Labels:** Secondary metadata and category titles use small, uppercase labels with increased letter spacing to distinguish them from actionable body text.
- **Numeric Data:** For inventory counts and resource requirements, a monospaced font (JetBrains Mono) may be used to ensure numbers align perfectly in vertical lists and tables.

## Layout & Spacing
The layout follows a **Fluid Grid** system that prioritizes horizontal scanning.

- **Desktop:** A 12-column grid with a maximum content width of 1440px. Components are grouped into logical "Modules" (e.g., Inventory, Goal Tracker, Daily Checklist).
- **Mobile:** A single-column stack with 16px side margins. Horizontal scrolling is permitted for wide data tables to prevent text wrapping.
- **Density:** The system uses a 4px baseline. Vertical padding is kept tight (`sm` or `md`) to allow as many data rows as possible to appear "above the fold." Large gaps are avoided to keep related crafting components visually grouped.

## Elevation & Depth
Depth is conveyed through **Tonal Layering** rather than shadows. This maintains the "flat utility" look while clearly defining hierarchy.

- **Level 0 (Canvas):** Darkest background color.
- **Level 1 (Cards/Panels):** Raised one step lighter. Used for primary content containers.
- **Level 2 (Inputs/Hover):** Lighter still. Used for interactive fields or to highlight a row on hover.
- **Outlines:** Subtle, 1px low-contrast borders (`Slate 700`) are used to define card boundaries where tonal shifts are too subtle for accessibility. No heavy shadows or glows are used, ensuring the UI feels like a professional tool.

## Shapes
The shape language is **Refined and Structured**.

- **Primary Corners:** A consistent `0.5rem` (8px) radius is applied to all cards, buttons, and input fields. This softens the technical nature of the app without making it feel overly "bubbly" or juvenile.
- **Small Elements:** Tooltips and tags use a smaller `4px` radius to maintain precision.
- **Icons:** Use a consistent 2px stroke weight with slightly rounded joins to match the typography and container corners.

## Components
- **Crafting Cards:** Large containers with a header (Title + Icon), a progress bar, and a nested list of requirements. Use a Level 1 surface.
- **Resource Chips:** Small, compact indicators showing an item icon and a count (e.g., `12/50`). Color code the background of the chip based on whether the requirement is met.
- **Buttons:**
    - *Primary:* Solid Blue-800 with white text.
    - *Secondary:* Ghost style with Slate-700 border.
    - *Actionable Items:* Rows in a list should change background color on hover to indicate they can be clicked for more detail.
- **Inventory Inputs:** Clean fields with internal labels. Use monospaced fonts for numerical input to assist in quick comparative scanning.
- **Progress Bars:** Thin (4px - 6px) tracks with high-contrast fills. Use color to indicate state: Green (Complete), Blue (Active), Red (Insufficient).
- **Checkboxes:** Square with a 2px radius. When checked, the entire list item row should receive a subtle desaturated background tint to show "Completion" at a glance.