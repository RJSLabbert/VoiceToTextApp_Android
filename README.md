# VoiceToText Meeting Minutes App 📱🤖

Android Kotlin app: **Voice record/type transcript → Gemini AI generates minutes & action items**.

[![Live APK](https://img.shields.io/badge/Download-APK-green?style=for-the-badge)](https://github.com/RJSLabbert/VoiceToTextApp_Android/releases)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.22-orange?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Gemini AI](https://img.shields.io/badge/Gemini-1.5_Flash-blue?logo=googleai&logoColor=white)](https://ai.google.dev)

## 🎯 Demo
![App Demo](demo.gif) *(Record emulator > Upload GIF/APK screenshot.)*

## ✨ Features
- 🎤 **Voice-to-Text**: SpeechRecognizer (real-time).
- 📝 **Transcript Edit**: Paste/edit input.
- 🧠 **AI Minutes**: Gemini summarizes: Key points, decisions, actions (owners/deadlines).
- ⚡ **Coroutines**: Non-blocking API calls.
- 📱 **Permissions**: Mic/Internet handled.

## 🛠️ Tech Stack
- Kotlin 1.9+
- Android SDK 34 / Min 24
- Gemini GenerativeAI lib
- SpeechRecognizer

## 🚀 Quick Setup
1. Clone:

 ## 🔧 Troubleshooting (Exact Error Logs + Fixes)

<details>
<summary>Click to Expand All Logs</summary>

### 1. Manifest Merger Failed (Multiple Errors)
**Exact Log** (Build tab): Execution failed for task ':app:processDebugMainManifest'. Manifest merger failed with multiple errors, see logs

**Fix**: 
- Add `xmlns:tools="http://schemas.android.com/tools"`.
- `<application tools:replace="android:allowBackup,android:theme,android:usesCleartextTraffic">`.
- `android:exported="true"` on activities.
- Clean `app/build` > Rebuild.

### 2. JAVA_HOME Invalid
**Exact Log**: ERROR: JAVA_HOME is set to an invalid directory: C:\Program Files\Java\jdk1.8.0_281\bi

**Fix**:
- `gradle.properties`: `org.gradle.java.home=C:\\Program Files\\Android\\Android Studio\\jbr`
- User Env Vars > JAVA_HOME = Studio JBR.
- JDK17 install if needed.
