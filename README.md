


# 🗣️ Smart Voice Chat (AI Voice Assistant)

Smart Voice Chat is a sleek AI-powered voice assistant Android application that allows users to interact through natural voice conversations. Built using **Jetpack Compose**, **OpenRouter (ChatGPT/GPT-4 compatible APIs)**, and **Text-to-Speech / Speech-to-Text**, this app creates a smooth, intuitive, and conversational user experience.

> 🔁 **Branch**: `development` — Single Voice Answering

---

## 🚀 Features

- 🎤 **Tap to Speak**  
  Natural interaction by tapping the mic to speak.

- 🤖 **AI-Powered Responses**  
  Uses OpenRouter API for real-time intelligent answers.

- 🔊 **Text-to-Speech Playback**  
  Listens to AI responses read aloud instantly.

- 💬 **Chat Interface**  
  Simple and clean chat screen with user/AI bubbles.

- 🛑 **Playback Control**  
  Allows user to pause/stop speech while the AI is speaking.

- 🧠 **Reactive ViewModel Architecture**  
  Clean separation of UI logic and business logic.

---

## 📸 Screenshot

<p align="center">
  <img src="https://github.com/user-attachments/assets/ba6372cd-6a71-4716-a630-d9d5008a9c6a" alt="Chat Screen" width="300"/>
  <img src="https://github.com/user-attachments/assets/2d8cd5ad-443c-4fd8-b474-8277f9636f85" alt="Chat Screen" width="300"/>
  <img src="https://github.com/user-attachments/assets/7f8e6068-a638-4532-bc14-7c526f6db92a" alt="Chat Screen" width="300"/>
  <img src="https://github.com/user-attachments/assets/d18978ee-a717-4c91-97e3-95ba8f43a459" alt="Chat Screen" width="300"/>
</p>

---

## 🧩 Tech Stack

- 🧱 **Jetpack Compose** — Declarative UI
- 🗣️ **Android SpeechRecognizer** — Voice input
- 🔊 **TextToSpeech** — AI voice playback
- 🌐 **OpenRouter API** — Natural language backend (GPT-4 / ChatGPT)
- 🧪 **MVVM Architecture** — ViewModel & Clean Repository Pattern
- ⚙️ **Kotlin Coroutines & Flow** — Reactive state management

---

## 🔐 OpenRouter API Key

1. Go to [https://openrouter.ai](https://openrouter.ai) and sign in.
2. Generate your API key.
3. Add it in your `ChatRepository.kt`:
   ```kotlin
   private val apiKey = "Bearer YOUR_API_KEY_HERE"
