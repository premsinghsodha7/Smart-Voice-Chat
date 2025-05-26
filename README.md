


# 🗣️ Smart Voice Chat (AI Voice Assistant)

Smart Voice Chat is a sleek AI-powered voice assistant Android application that allows users to interact through natural voice conversations. Built using **Jetpack Compose**, **OpenRouter (ChatGPT/GPT-4 compatible APIs)**, and **Text-to-Speech / Speech-to-Text**, this app creates a smooth, intuitive, and conversational user experience.

> 🔁 **Branch**: `development_voice` — Single Voice Answering

---

## 🚀 Features

- 🎤 **Tap & Hold to Speak**  
  Natural interaction by holding the mic to speak.

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
  <img src="https://github.com/user-attachments/assets/e377607a-3d83-4785-bfbb-04213c936593" alt="Chat Screen" width="300"/>
  <img src="https://github.com/user-attachments/assets/80b4eb2d-e7dc-45a9-8ae2-44be3def490d" alt="Chat Screen" width="300"/>
  <img src="https://github.com/user-attachments/assets/2e428d0c-daa4-403d-93cb-6dfab0543b64" alt="Chat Screen" width="300"/>
  <img src="https://github.com/user-attachments/assets/ca24fc20-49a7-46d7-b0da-2e643288a9a0" alt="Chat Screen" width="300"/>
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


🛠️ Getting Started
Prerequisites
Android Studio Hedgehog or later

Android SDK 33+

Internet permission in AndroidManifest.xml

Clone the Repository
bash
Copy
Edit
git clone https://github.com/premsinghsodha7/Smart-Voice-Chat.git
cd Smart-Voice-Chat
git checkout development_voice
Run on Device
Open the project in Android Studio.

Add your OpenRouter API key.

Run the app on a physical/emulator device.

🔀 Other Branches
development – Full ChatBot with two-way speech and message interactions.


