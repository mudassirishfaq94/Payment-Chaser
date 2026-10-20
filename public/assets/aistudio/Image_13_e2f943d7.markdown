---
name: Payment Chaser Design System
colors:
  surface: '#f7f9fb'
  surface-dim: '#d8dadc'
  surface-bright: '#f7f9fb'
  surface-container-lowest: '#ffffff'
  surface-container-low: '#f2f4f6'
  surface-container: '#eceef0'
  surface-container-high: '#e6e8ea'
  surface-container-highest: '#e0e3e5'
  on-surface: '#191c1e'
  on-surface-variant: '#45464c'
  inverse-surface: '#2d3133'
  inverse-on-surface: '#eff1f3'
  outline: '#76777d'
  outline-variant: '#c6c6cd'
  surface-tint: '#575e70'
  primary: '#000000'
  on-primary: '#ffffff'
  primary-container: '#141b2b'
  on-primary-container: '#7d8497'
  inverse-primary: '#c0c6db'
  secondary: '#4648d4'
  on-secondary: '#ffffff'
  secondary-container: '#6063ee'
  on-secondary-container: '#fffbff'
  tertiary: '#000000'
  on-tertiary: '#ffffff'
  tertiary-container: '#002113'
  on-tertiary-container: '#009668'
  error: '#ba1a1a'
  on-error: '#ffffff'
  error-container: '#ffdad6'
  on-error-container: '#93000a'
  primary-fixed: '#dce2f7'
  primary-fixed-dim: '#c0c6db'
  on-primary-fixed: '#141b2b'
  on-primary-fixed-variant: '#404758'
  secondary-fixed: '#e1e0ff'
  secondary-fixed-dim: '#c0c1ff'
  on-secondary-fixed: '#07006c'
  on-secondary-fixed-variant: '#2f2ebe'
  tertiary-fixed: '#6ffbbe'
  tertiary-fixed-dim: '#4edea3'
  on-tertiary-fixed: '#002113'
  on-tertiary-fixed-variant: '#005236'
  background: '#f7f9fb'
  on-background: '#191c1e'
  surface-variant: '#e0e3e5'
typography:
  display-lg:
    fontFamily: Inter
    fontSize: 40px
    fontWeight: '700'
    lineHeight: 48px
    letterSpacing: -0.02em
  display-md:
    fontFamily: Inter
    fontSize: 32px
    fontWeight: '700'
    lineHeight: 40px
    letterSpacing: -0.02em
  headline-lg:
    fontFamily: Inter
    fontSize: 28px
    fontWeight: '600'
    lineHeight: 36px
    letterSpacing: -0.01em
  headline-md:
    fontFamily: Inter
    fontSize: 24px
    fontWeight: '600'
    lineHeight: 32px
    letterSpacing: -0.01em
  headline-sm:
    fontFamily: Inter
    fontSize: 20px
    fontWeight: '600'
    lineHeight: 28px
  title-lg:
    fontFamily: Inter
    fontSize: 18px
    fontWeight: '600'
    lineHeight: 24px
  title-md:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '600'
    lineHeight: 22px
  title-sm:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '600'
    lineHeight: 20px
  body-lg:
    fontFamily: Inter
    fontSize: 16px
    fontWeight: '400'
    lineHeight: 24px
  body-md:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '400'
    lineHeight: 20px
  body-sm:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '400'
    lineHeight: 16px
  label-lg:
    fontFamily: Inter
    fontSize: 14px
    fontWeight: '500'
    lineHeight: 20px
    letterSpacing: 0.01em
  label-md:
    fontFamily: Inter
    fontSize: 12px
    fontWeight: '500'
    lineHeight: 16px
    letterSpacing: 0.02em
  label-sm:
    fontFamily: Inter
    fontSize: 11px
    fontWeight: '600'
    lineHeight: 14px
    letterSpacing: 0.04em
rounded:
  sm: 0.25rem
  DEFAULT: 0.5rem
  md: 0.75rem
  lg: 1rem
  xl: 1.5rem
  full: 9999px
spacing:
  gutter: 1rem
  margin: 1rem
  space-xs: 0.25rem
  space-sm: 0.5rem
  space-md: 1rem
  space-lg: 1.5rem
  space-xl: 2rem
---

## Brand & Style

The design system establishes a high-trust, proactive, and frictionless mobile fintech experience tailored for Android. The emotional target is calm control and effortless resolution: debt collection and accounts receivable tracking often trigger anxiety, so the visual language counteracts this with deliberate structure, soft geometry, and decisive status communication.

The aesthetic fuses Material Design 3 structural primitives with contemporary high-end fintech refinement. Interfaces rely on generous touch targets (minimum 48dp), deep tactile clarity, clean container contrast, and focused semantic feedback. Motion and elevation communicate transient state changes (bottom sheets, interactive card expansions, sticky bottom bars) without extraneous visual clutter.

## Colors

The color system relies on high contrast between deep structure and purposeful chromatic accents. Semantic states are strictly reserved for operational balance:

- **Primary (`#111827`)**: Grounding foundation for key interactions, high-emphasis text, top app bars, and high-priority action triggers.
- **Secondary / Accent (`#6366F1`)**: Vibrant indigo driving active affordances, primary workflow focus, progress bars, and the central elevation action button.
- **Status Indicators**:
  - Success: Emerald (`#10B981`) for settled balances, cleared invoices, and positive metric indicators.
  - Overdue / Error: Coral Red (`#EF4444`) for past-due liabilities, transaction failures, and immediate alerts.
  - Warning: Amber (`#F59E0B`) for approaching due dates and pending approvals.
- **Surface Hierarchy**:
  - `background`: `#F8FAFC` (Cool slate tint)
  - `surface`: `#FFFFFF` (Base cards, elevated sheets, input fields)
  - `surface_container`: `#F1F5F9` (Grouped list items, inactive toggle tracks, subtle pill tags)
  - `surface_container_high`: `#E2E8F0` (Borders, divider rules, subtle interactive states)
- **Text & Iconography**:
  - High emphasis (`#0F172A`)
  - Medium emphasis / secondary (`#64748B`)
  - Low emphasis / tertiary (`#94A3B8`)

## Typography

The typographic hierarchy implements an adapted Material 3 type scale rendered in Inter. It is calibrated specifically for handheld viewing distances and quick analytical parsing.

- **Display & Large Numerics (`display-lg`, `display-md`)**: Reserved for total outstanding balances, metric summary totals, and transaction confirmation screens. Always set with tight tabular spacing to maintain numerical alignment across variable widths.
- **Headlines & Titles (`headline-*`, `title-*`)**: Form screen titles, bottom sheet headers, and individual invoice counterparty titles. Weights default to Semi-Bold (600) to ground hierarchical groupings.
- **Body (`body-*`)**: Applied across ledger descriptions, transaction audit logs, and instructional guidance. Line heights allow for generous multiline breathing room.
- **Labels (`label-*`)**: Dedicated to interactive elements, pill indicators, column headers, micro-metadata, and status flags. Tabular figures must be enabled for dates, amounts, and countdown timestamps.

## Layout & Spacing

Layout geometry adheres to the 4dp/8dp incremental spatial grid standard on Android mobile devices (360×800dp base viewport reference):

- **Safe Zones & Device Frame**:
  - Status Bar: 48dp top inset clearance.
  - Gesture Navigation Bar: 24dp bottom inset clearance + 16dp buffer for system pill handle.
  - Screen Canvas: 16dp horizontal edge margins (`margin`) to maximize data density while preventing edge-clipping.
- **Grids & Composition**:
  - Primary single-column fluid mobile flow with internal horizontal scroll ribbons for status categories.
  - Card-to-card vertical gap: 12dp to 16dp (`space-md`).
  - Section-to-section block rhythm: 24dp to 32dp (`space-xl`).
- **Touch Bounds**:
  - All interactive elements retain a rigid minimum target size of 48×48dp regardless of visible icon or label boundaries.

## Elevation & Depth

Visual hierarchy combines tonal layering with precise, low-opacity ambient shadows to avoid muddy contrast on modern OLED and high-density mobile screens:

- **Level 0 (Flat Canvas)**: Canvas baseline (`#F8FAFC`). No shadow.
- **Level 1 (Structural Cards & List Containers)**:
  - Background: `#FFFFFF`
  - Border: 1dp solid `#E2E8F0`
  - Shadow: `0px 1px 3px rgba(15, 23, 42, 0.04), 0px 4px 8px rgba(15, 23, 42, 0.02)`
- **Level 2 (Active/Lifted Cards & Floating Toolbars)**:
  - Background: `#FFFFFF`
  - Shadow: `0px 4px 12px rgba(15, 23, 42, 0.06), 0px 12px 24px rgba(15, 23, 42, 0.04)`
- **Level 3 (Sticky Bottom Navigation)**:
  - Background: `#FFFFFF` at 92% opacity with `backdrop-filter: blur(12px)`.
  - Border Top: 1dp solid `#F1F5F9`.
  - Shadow: `0px -4px 16px rgba(15, 23, 42, 0.03)`.
- **Level 4 (Center Action FAB & Floating Menus)**:
  - Background: `#6366F1`
  - Shadow: `0px 8px 20px rgba(99, 102, 241, 0.35)`
- **Level 5 (Modal Bottom Sheets & Dialogs)**:
  - Background: `#FFFFFF`
  - Scrim: `#0F172A` at 40% opacity.
  - Shadow: `0px -8px 32px rgba(15, 23, 42, 0.16)`

## Shapes

The interface embraces a rounded, contemporary Material 3 corner architecture:

- **Full Rounded / Capsule (9999px)**: Buttons, search inputs, status badges, filter chips, and the central FAB.
- **Rounded 3XL (24px - 28px)**: Bottom sheet surfaces (top corners), primary summary balance hero cards, and modal dialogs.
- **Rounded 2XL (16px)**: Transaction cards, invoice ledger items, and modular analytical dashboards.
- **Rounded MD (8px - 10px)**: Progress track caps, inner icon badge containers, and small notification toasts.

## Components

### Bottom Navigation & Center Action
- **Navigation Bar**: Docked to bottom, height 80dp (including gesture insets), displaying 5 destinations: *Dashboard*, *Invoices*, *[Chase Action FAB]*, *Debtors*, and *Settings*.
- **Center Action Button**: 56×56dp circular FAB elevated to Level 4, overlapping the bottom bar by 24dp. Color is Vibrant Indigo (`#6366F1`) with a white dynamic icon (`+` or lightning chase trigger).
- **Navigation Items**: 24dp icon paired with 11dp label (`label-sm`). Inactive destinations render in `#64748B`; active destination renders in `#111827` backed by an indigo pill indicator (`#6366F1` at 12% opacity).

### Cards (Invoices & Balance Summaries)
- **Invoice Card**: Level 1 elevation, rounded-2xl (`16px`), 1dp `#E2E8F0` border. Contains avatar/client logo, counterparty name (`title-md`), relative due date tag, total balance (`headline-sm`), and a quick action swipe/tap affordance.
- **Hero Balance Card**: Deep Navy background (`#111827`), white text, rounded-3xl (`24px`). Displays primary metric (`display-lg`), total overdue amount with Coral Red indicator, and secondary action pills with translucent white borders (`rgba(255, 255, 255, 0.15)`).

### Buttons
- **Primary**: Pill-shaped (`9999px`), height 48dp minimum, background `#111827` or `#6366F1`, text `#FFFFFF` (`label-lg`). Ripple effect on touch.
- **Tonal / Secondary**: Pill-shaped, background `#F1F5F9`, text `#0F172A`.
- **Destructive**: Pill-shaped, background `#EF4444` at 10% opacity, text `#EF4444`.

### Chips & Status Badges
- **Status Badges**: Capsule pill geometry with 6dp horizontal padding and 2dp vertical padding.
  - *Paid*: Emerald background (`#10B981` at 12% opacity), `#047857` text.
  - *Overdue*: Coral background (`#EF4444` at 12% opacity), `#B91C1C` text.
  - *Pending / Draft*: Amber background (`#F59E0B` at 12% opacity), `#B45309` text.
- **Filter Chips**: Height 36dp, outline 1dp solid `#E2E8F0`, rounded-full, with an active state shifting background to `#111827` and label to `#FFFFFF`.

### Input Fields
- **Container**: Outlined style with 16dp roundedness, background `#FFFFFF`, border 1.5dp `#E2E8F0`. Focus state shifts border to 2dp `#6366F1`.
- **Text & Placeholder**: Placeholder in `#94A3B8`, active value in `#0F172A`. Minimum tap height 56dp with floating labels adhering to M3 interaction specs.

### Bottom Sheet (Chaser Workflow)
- **Container**: Rounded top corners at 28px (`rounded-3xl`), Level 5 elevation, with an internal drag handle (32×4dp pill, `#CBD5E1`) centered at the top.
- **Content**: Dedicated to rapid payment reminder drafting, custom SMS/Email payment link triggers, and direct debtor settlement logging.