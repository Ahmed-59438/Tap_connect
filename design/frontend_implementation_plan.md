# TapConnect Master Plan: UX Flow, Backend Schema & Frontend Implementation

This document serves as the master reference for the TapConnect development team, detailing how the user interacts with the app, how data is structured on the backend, and the exact steps to build the frontend.

---

## 1. UI/UX App Flow (User Journey)

The application flow is designed to be minimal, keeping interactions under 2-3 clicks to reach the core networking features.

### A. Onboarding & Authentication
1. **Splash Screen**: Animated TapConnect logo.
2. **Login/Signup**: 
   - Options: Google Sign-in or Phone Number (OTP).
   - "Continue Offline" (MVP local-first mode).
3. **Permissions Gate**: 
   - Prompts for Bluetooth, Nearby Devices, and Location permissions with clear explanations ("Why we need this").

### B. Profile Setup (First Time)
1. **Basic Info**: Name, Role/Title, Organization.
2. **Bio & Interests**: Short text bio, selection of predefined interest tags (e.g., #AI, #Startups, #Design).
3. **Social Links**: Inputs for LinkedIn, Twitter, GitHub.
4. **Privacy Defaults**: Toggle switches for what to share publicly vs. privately.

### C. Main Dashboard (Discovery Mode)
1. **Status Header**: Large toggle for "Networking Mode" (ON/OFF).
2. **Radar View**: 
   - When ON: Pulsing animation. List of "Nearby Users" populates via BLE.
   - Users are ranked by **AI Prioritization** (matching interests).
3. **Bottom Navigation**: `[ Radar (Home) | Connections | My Profile ]`

### D. NFC Tap-to-Connect Flow
1. User A and User B bring phones together.
2. **Haptic Feedback**: Phone vibrates.
3. **Connection Modal**: Slides up with "Connect with [Name]?"
4. **Action**: Tap "Accept".
5. **Success Screen**: Shows AI-generated Icebreaker (e.g., *"You both love Kotlin, ask them about Compose!"*).

---

## 2. Backend Database Schema (PostgreSQL / Supabase)

Your friend building the FastAPI/Supabase backend should use this schema.

### Table: `users`
Stores core identity and profile data.
```sql
CREATE TABLE users (
    user_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(255) UNIQUE,
    full_name VARCHAR(100) NOT NULL,
    bio TEXT,
    role VARCHAR(100),
    organization VARCHAR(100),
    interests TEXT[], -- Array of strings
    social_links JSONB, -- e.g., {"linkedin": "url", "twitter": "url"}
    sharing_preferences JSONB, -- e.g., {"share_email": false}
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    last_active TIMESTAMP WITH TIME ZONE
);
```

### Table: `connections`
Records successful connections between two users.
```sql
CREATE TABLE connections (
    connection_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_a_id UUID REFERENCES users(user_id),
    user_b_id UUID REFERENCES users(user_id),
    connection_type VARCHAR(20), -- 'NFC' or 'BLE'
    ai_summary TEXT, -- Stored summary generated at time of connection
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_a_id, user_b_id) -- Prevent duplicate connection records
);
```

### Table: `discovery_sessions` (Optional for Real-time tracking)
Tracks who is currently broadcasting via BLE in a specific area.
```sql
CREATE TABLE discovery_sessions (
    session_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(user_id),
    location_geo GEOGRAPHY(Point), -- Optional: for event-based filtering
    is_active BOOLEAN DEFAULT TRUE,
    started_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);
```

---

## 3. Frontend Implementation Plan (Kotlin + Jetpack Compose)

This is your step-by-step roadmap for building the Android application.

### ✅ Phase 1: Project Foundation (Completed)
- [x] Initialized Gradle Version Catalog (`libs.versions.toml`).
- [x] Set up Material 3 Theme (`Theme.kt`, `Color.kt`, `Type.kt`).
- [x] Configured Room and DataStore dependencies.
- [x] Defined core directory structure (Clean Architecture).

### 🛠 Phase 2: Local Data & State Management (Current Focus)
- **Step 2.1**: Implement Room DAOs. Define the `ProfileEntity` and database creation logic.
- **Step 2.2**: Implement `SettingsDataStore` to persist the "Networking Mode" toggle state.
- **Step 2.3**: Create the `ProfileRepository` to act as the single source of truth between Room (local) and Retrofit (remote API).

### 🎨 Phase 3: Core UI & Navigation
- **Step 3.1**: Set up `NavHost` in `MainActivity.kt` defining routes: `Splash`, `Onboarding`, `ProfileSetup`, `Dashboard`.
- **Step 3.2**: Build the `ProfileSetupScreen` using Material 3 `OutlinedTextField` and custom Glassmorphism cards.
- **Step 3.3**: Build the `NetworkingScreen` (Dashboard) with the pulsing radar animation using Compose's `InfiniteTransition`.

### 📡 Phase 4: Hardware Integration (NFC & BLE)
- **Step 4.1**: Implement `NfcManager.kt` using `NfcAdapter.enableReaderMode`. Extract payload (User ID) from NDEF messages.
- **Step 4.2**: Implement `BleScanner.kt` and `BleAdvertiser.kt` using `BluetoothLeScanner`. Handle runtime permissions rigorously.
- **Step 4.3**: Connect Hardware managers to `DiscoveryViewModel` to update the UI state when a device is found.

### 🧠 Phase 5: API & AI Integration
- **Step 5.1**: Set up Retrofit interface `TapConnectApi.kt` matching the FastAPI endpoints.
- **Step 5.2**: Implement the "Connection Success" logic: Send both User IDs to the backend, receive the AI-generated Icebreaker, and display it in the Compose UI.

---
*Document Version: 1.0 | Target: Android API 33+*
