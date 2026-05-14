# TapConnect Design System & Brand Guidelines

## 1. Brand Identity
**Mission**: To make real-world networking as effortless as a single tap.
**Tone**: Futuristic, Reliable, Effortless, and Human-centric.

## 2. Color Palette (Dark Mode First)
We use a "Deep Sea" palette with "Neon" accents for high contrast and a premium feel.

| Token | Name | Hex Code | Usage |
| :--- | :--- | :--- | :--- |
| `primary` | Electric Indigo | `#6366F1` | Buttons, Active States, Brand |
| `primary-variant` | Deep Violet | `#4F46E5` | Hover/Pressed States |
| `secondary` | Mint Green | `#10B981` | Success, Connections, Icons |
| `background` | Midnight Blue | `#0F172A` | Main App Background |
| `surface` | Deep Slate | `#1E293B` | Cards, Modals |
| `on-primary` | White | `#FFFFFF` | Text on Primary |
| `on-background` | Slate-100 | `#F1F5F9` | Primary Body Text |
| `on-surface` | Slate-400 | `#94A3B8` | Secondary Text |

## 3. Typography
**Font Family**: `Inter` (Google Fonts)
- **Display Large**: 32sp / Bold / Letter-spacing: -0.5px
- **Headline Medium**: 24sp / Semi-Bold
- **Title Small**: 16sp / Medium
- **Body Large**: 16sp / Regular / Line-height: 24sp
- **Label Small**: 12sp / Medium / All Caps (for metadata)

## 4. Design Tokens (JSON)
```json
{
  "spacing": {
    "none": 0,
    "xs": 4,
    "sm": 8,
    "md": 16,
    "lg": 24,
    "xl": 32
  },
  "shape": {
    "card-radius": 16,
    "button-radius": 12,
    "avatar-radius": "50%"
  },
  "elevation": {
    "none": 0,
    "low": 2,
    "medium": 8,
    "high": 16
  }
}
```

## 5. Visual Principles
- **Glassmorphism**: Use semi-transparent surfaces with background blur for secondary elements.
  - *Example*: `background: rgba(255, 255, 255, 0.05); blur: 10px;`
- **Soft Shadows**: Avoid harsh blacks. Use tinted shadows (e.g., `#00000020`).
- **Gradients**: Use subtle linear gradients for buttons (Electric Indigo to Deep Violet).

## 6. Components Preview
- **Cards**: Surface color, 1px border (`#FFFFFF10`), 16dp radius.
- **Buttons**: Primary color, 12dp radius, white text, bold weight.
- **Inputs**: Background color, 1px border (`#6366F150`), 8dp padding.
