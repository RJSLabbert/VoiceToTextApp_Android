# VoiceToText Meeting Minutes App 📱🤖

Android Kotlin app: **Voice record/type transcript → Gemini AI generates minutes & action items**.

[![Live APK](https://img.shields.io/badge/Download-APK-green?style=for-the-badge)](https://github.com/RJSLabbert/VoiceToTextApp_Android/releases)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-orange?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Gemini AI](https://img.shields.io/badge/Gemini-1.5_Flash-blue?logo=googleai&logoColor=white)](https://ai.google.dev)
[![Status](https://img.shields.io/badge/Status-Discontinued-red?style=flat-square)]()

---

## 🎙️ Why I built this

I wanted to build something I could monetize. Voice to AI summaries felt like a trend worth chasing. I learned a lot about Kotlin, the Gemini API, and Android development in the process — but the passion wasn't there. The project died when the motivation did.

**Status: Discontinued.** The codebase and lessons learned are kept here as a reference.

---

## ✨ Features

- 🎤 **Voice-to-Text** — SpeechRecognizer real-time input
- 📝 **Transcript Edit** — paste or edit input before processing
- 🧠 **AI Minutes** — Gemini summarizes key points, decisions, and action items with owners and deadlines
- ⚡ **Coroutines** — non-blocking API calls
- 📱 **Permissions** — mic and internet handled cleanly

---

## 🛠️ Tech Stack

- Kotlin 1.9+
- Android SDK 34 / Min 24
- Gemini GenerativeAI library
- SpeechRecognizer

---

## 🚀 Quick Setup

1. Clone the repo:
```bash
git clone https://github.com/RJSLabbert/VoiceToTextApp_Android.git
```
2. Open in Android Studio
3. Add your Gemini API key to `local.properties`:
```
GEMINI_API_KEY=your_key_here
```
4. Build and run on device or emulator

---

## 🔧 Troubleshooting

<details>
<summary>Click to Expand</summary>

### 1. Manifest Merger Failed

**Error:**
```
Execution failed for task ':app:processDebugMainManifest'.
Manifest merger failed with multiple errors, see logs
```

**Fix:**
- Add `xmlns:tools="http://schemas.android.com/tools"` to manifest
- Add `<application tools:replace="android:allowBackup,android:theme,android:usesCleartextTraffic">`
- Add `android:exported="true"` on activities
- Clean `app/build` then rebuild

### 2. JAVA_HOME Invalid

**Error:**
```
ERROR: JAVA_HOME is set to an invalid directory: C:\Program Files\Java\jdk1.8.0_281\bin
```

**Fix:**
- In `gradle.properties` add: `org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr`
- Update User Environment Variables → JAVA_HOME to point to Android Studio JBR
- Install JDK17 if needed

</details>

---

## 👤 Author

**RJS Labbert**

- GitHub: [@RJSLabbert](https://github.com/RJSLabbert)
- Blog: [rocksolidcode.co.za](https://rocksolidcode.co.za)
