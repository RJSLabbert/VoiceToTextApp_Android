# VoiceToText Meeting Minutes App 📱🤖

**Android app**: Record/type meeting → Speech-to-text → Gemini AI generates structured minutes & action items.

[![License](https://img.shields.io/badge/License-MIT-blue)](LICENSE)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.9-orange?logo=kotlin)](https://kotlinlang.org)
[![Gemini AI](https://img.shields.io/badge/Gemini-1.5_Flash-yellow?logo=google)](https://ai.google.dev)

## ✨ Features
- 🎤 **Voice Input**: SpeechRecognizer for live transcription.
- 📝 **Manual Edit**: Paste/type transcripts.
- 🧠 **AI Summary**: Gemini API → Bullets: Key points, decisions, actions (owners/deadlines).
- ⚡ **Fast**: 1-2s API response.
- 📱 **Responsive**: Emulator/device ready (API 24+).

## 🎥 Demo
![Demo GIF](demo.gif) *(Add: Record emulator Looper, upload.)*

**APK Download**: [VoiceToTextApp.apk](releases/VoiceToTextApp.apk) *(Build > Signed APK).*

## 🛠️ Tech Stack
| Category | Tech |
|----------|------|
| Language | Kotlin |
| UI | XML + ViewBinding |
| Speech | SpeechRecognizer |
| AI | Google Gemini (generativeai:0.5.0) |
| HTTP | OkHttp (implicit) |
| Coroutines | kotlinx.coroutines |

## 🚀 Quick Setup
1. **Clone**:
