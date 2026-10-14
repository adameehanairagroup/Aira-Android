# ⚡ Aira for Android (Project AiraPhone)

> **Next-Generation Sovereign Mobile AI OS Layer & Proactive Assistant**  
> *Engineered by Er. Adam Eehan (Founder & CEO, Aira Group of Technology)*

[![Build APK with GitHub Actions](https://github.com/adameehanaira/Aira-Android/actions/workflows/build_apk.yml/badge.svg)](https://github.com/adameehanaira/Aira-Android/actions/workflows/build_apk.yml)
[![License: Apache 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![Platform: Android](https://img.shields.io/badge/Platform-Android_12+-green.svg)](https://developer.android.com)

---

## 🌟 Overview

**Aira for Android** transforms any standard Android smartphone into an **AI-Native Sovereign Device**. Bypassing the limitations of conventional assistants, Aira runs an ultra-low-power on-device acoustic wake-word listener, zero-STT phonetic resonance matching, voice biometric speaker identification, and deep OS automation via Android Accessibility Services.

```
┌────────────────────────────────────────────────────────┐
│           ⚡ AIRA FOR ANDROID ARCHITECTURE             │
├──────────────────────────┬─────────────────────────────┤
│ 🎙️ Ultra-Low-Power Audio │  Offline Wake-Word ("Aira!")│
│ 🛡️ Biometric Gatekeeper  │  Voiceprint Identity Match  │
│ 🪄 Assistant Overlay     │  Floating Glassmorphic View │
│ 🕹️ Accessibility Service │  60+ OS Actions (No Root!)  │
│ 🧠 Multi-Model Brain     │  Gemini 3.8 / Groq Fallback │
│ ☁️ Cloud CI/CD           │  GitHub Actions APK Builder │
└──────────────────────────┴─────────────────────────────┘
```

---

## 🚀 Key Features

### 1. 🎙️ Battery-Saving Wake-Word ("Aira!")
- Runs an ultra-lightweight on-device wake-word detection engine (< 1% battery per day).
- Keeps the main neural network in deep sleep until Er. Adam Eehan calls *"Aira!"*.

### 2. 🛡️ Voice Biometric Identity Gatekeeper (Zero-Trust)
- Extracts the speaker's vocal tract resonance and pitch embeddings.
- Validates identity against `boss_voiceprint.npy` using Cosine Similarity ($\ge 0.85$).
- **Anti-Spoofing:** Friends, strangers, or ambient videos calling "Aira" are automatically rejected!

### 3. 🪄 Floating Assistant Popup Overlay (`popup()`)
- Powered by `android.permission.SYSTEM_ALERT_WINDOW` (`TYPE_APPLICATION_OVERLAY`).
- When the wake-up word is triggered, a sleek glassmorphic assistant bottom sheet floats up instantly over any active app (YouTube, WhatsApp, Instagram, Games, Lockscreen) with glowing audio waveform visualizer and quick action chips.

### 4. 🕹️ Android Accessibility God-Mode (Zero-Root Automation)
- **Instant Screen Lock** (`GLOBAL_ACTION_LOCK_SCREEN`)
- **Home & Back Navigation** (`GLOBAL_ACTION_HOME`, `GLOBAL_ACTION_BACK`)
- **Stealth Screenshot** (`GLOBAL_ACTION_TAKE_SCREENSHOT`)
- **Native Flashlight Toggle** via `CameraManager` (1ms response)
- **Media & Call Volume Tuning** via `AudioManager`
- **WhatsApp & App Automation** (Automated messaging, call dispatching)

### 5. 👁️ Proactive Autonomous Agency
- **Smart Notification Intelligence:** Intercepts critical college/class teacher notifications and delivers spoken voice summaries without requiring a manual prompt.
- **Battery & Thermal Guard:** Automatically engages power saver and dims screen when battery drops below 15%.
- **Absentee College Reporter:** Generates and verifies student absentee reports at scheduled morning cut-off times.

---

## 🛠️ How the Floating Assistant Popup Works (`popup()`)

To show a floating assistant popup over other apps when the wake word is detected:

```kotlin
// In AiraAssistantPopup.kt
val params = WindowManager.LayoutParams(
    WindowManager.LayoutParams.MATCH_PARENT,
    WindowManager.LayoutParams.WRAP_CONTENT,
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    PixelFormat.TRANSLUCENT
).apply {
    gravity = Gravity.BOTTOM
}

windowManager.addView(floatingAiraView, params)
```

Requires user permission: `Settings.ACTION_MANAGE_OVERLAY_PERMISSION` ("Display over other apps").

---

## ☁️ Zero-PC Cloud Build Pipeline (GitHub Actions)

No local PC or heavy Android Studio required! Committing code triggers `.github/workflows/build_apk.yml`:
1. Sets up Ubuntu 24.04 runner with JDK 21 and Android SDK 35.
2. Compiles the APK via `./gradlew assembleDebug`.
3. Publishes `Aira-Android-Debug.apk` as a downloadable artifact directly to your phone.

---

## 📜 License

```
Copyright 2026 Er. Adam Eehan - Aira Group of Technology
Licensed under the Apache License, Version 2.0.
```
