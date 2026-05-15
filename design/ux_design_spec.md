# TapConnect — Complete UI/UX Design Specification
**Version**: 2.0 | **Philosophy**: Minimal, Purposeful, Premium Dark

> **Design Principles:**
> 1. One Screen, One Job
> 2. ≤2 Taps to Core Value (Radar screen)
> 3. Dark Mode First — Background `#0F172A`, Surfaces `#1E293B`
> 4. Silence is Design — Whitespace is intentional

---

## Navigation Architecture

```
App Launch → Splash Screen
    ├── [First Time] → Onboarding (3 slides) → Login → Permissions Gate
    │       → Profile Setup Step 1 → Profile Setup Step 2 → Radar Dashboard
    └── [Returning User] → Radar Dashboard
            ├── [Tap User] → User Profile Detail → Icebreaker Success
            ├── [Bottom Nav] → Connections List
            └── [Bottom Nav] → My Profile → Edit Profile / Privacy Controls
```

---

## Screens

### S1. Splash Screen (New)
- Centered logo + tagline + animated 2s progress bar
- Auto-navigates: Onboarding or Radar (returning user)

### S2. Onboarding (3 Slides) (New)
- Slide 1: Discover nearby professionals (BLE radar)
- Slide 2: Connect in one tap (NFC)
- Slide 3: AI Icebreakers
- Skip/Next → Login Screen

### S3. Login / Signup (New)
- Google Sign-In button (primary)
- Phone + OTP field
- "Continue Offline" text link for local-first MVP

### S4. Permissions Gate (New)
- 3 permission cards: Location, Bluetooth, NFC
- Each card explains WHY it's needed
- One "Grant Permissions" primary button

### S5. Profile Setup — Step 1/2 (Redesign)
- CHANGES: 2-step split, labels above fields, progress bar, avatar upload
- Fields: Name, Role, Organization
- Next button disabled until Name filled

### S6. Profile Setup — Step 2/2 (New - Split)
- Fields: Bio (150 char), Interests (chip selector), Social Links
- Chip selector with 12 predefined tags (#AI, #Kotlin, #Design, etc.)
- Save → Radar Dashboard

### S7. Networking Radar Dashboard (Redesign)
- CHANGES: Add top bar with avatar+logo, add bottom nav, add count label
- Large networking toggle switch
- Pulsing radar with orbiting user avatars
- Bottom Nav: [Radar | Connections | My Profile]

### S8. Tap-to-Connect Bottom Sheet (New)
- Triggers on NFC/BLE detection
- Overlapping avatars: "You ↔ Sarah"
- 8s auto-dismiss progress bar
- Spring animation slide-up, haptic pulse on appear
- Connect (Mint Green) / Dismiss buttons

### S9. Icebreaker Success Screen (New)
- Large ✅ icon, confetti animation
- AI Icebreaker card with animated glow border
- CTAs: "View Profile" / "Back to Radar"

### S10. Connections List (New)
- Count header + search icon
- Filter chips: All / By Event / Recent
- Connection cards: Avatar, Name, Role, Timestamp
- Tap → User Profile Detail

### S11. User Profile Detail (New)
- Large avatar (100dp), name, role, org
- Social icon links row
- Bio and interest chips (read-only)
- Connected timestamp metadata

### S12. My Profile Screen (New)
- Own profile view as others see it
- Stats row: Connection count · Events attended
- Privacy Controls card CTA at bottom
- Edit button → Pre-filled Profile Setup

### S13. Privacy Controls (New)
- "Always Shared" section: Name, Role, Avatar (non-toggleable)
- "Optional" toggles: Email, LinkedIn, Phone, Bio
- "Discovery" toggles: Appear on Radar, Allow NFC

### S14. Error & Empty States (New)
- Empty Radar: "No one nearby yet" with static radar rings
- Bluetooth off: Yellow warning banner with Settings deep link
- No internet: Snackbar "Offline Mode — Showing cached data" (4s)

---

## Global Design Tokens

| Token | Value |
|-------|-------|
| spacing-xs | 4dp |
| spacing-sm | 8dp |
| spacing-md | 16dp |
| spacing-lg | 24dp |
| spacing-xl | 32dp |
| radius-card | 16dp |
| radius-button | 12dp |
| bottom-nav-height | 80dp |
| min-touch-target | 48dp |

---

## Implementation Priority

| Priority | Screen |
|----------|--------|
| P0 | Splash, Radar Redesign, Tap Modal, Icebreaker Success |
| P1 | Profile Setup (2-step), Connections List, Privacy Controls |
| P2 | Onboarding, Login, Profile Detail |
| P3 | Permissions Gate, Error States |
