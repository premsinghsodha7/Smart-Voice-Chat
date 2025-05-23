package com.compose.smartvoicechat.ui.screen

import android.content.Context
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compose.smartvoicechat.data.ChatRepository
import com.compose.smartvoicechat.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel(), TextToSpeech.OnInitListener {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    // For tracking which message is being read (by index or id)
    private val _readingMessageId = MutableStateFlow<String?>(null)
    val readingMessageId: StateFlow<String?> = _readingMessageId

    private var speechRecognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    // Initialize TTS, call this once from your UI (pass context)
    fun initializeTTS(context: Context) {
        if (tts == null) {
            tts = TextToSpeech(context.applicationContext, this)
        }
    }

    override fun onInit(status: Int) {
        isTtsReady = (status == TextToSpeech.SUCCESS)
        Log.d("ChatViewModel", "TTS initialized: $isTtsReady")
        if (isTtsReady) {
            tts?.language = Locale.getDefault()
        }
    }

    fun onMicTapped(context: Context) {
        if (_isListening.value) {
            stopListening()
        } else {
            startListening(context)
        }
    }

    private fun startListening(context: Context) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val intent = android.content.Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        }

        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onResults(results: Bundle) {
                val spokenText = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                spokenText?.let {
                    sendMessageToAI(it)
                }
                _isListening.value = false
            }
            override fun onReadyForSpeech(params: Bundle) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray) {}
            override fun onEndOfSpeech() {}
            override fun onError(error: Int) {
                _isListening.value = false
            }
            override fun onPartialResults(partialResults: Bundle) {}
            override fun onEvent(eventType: Int, params: Bundle) {}
        })

        speechRecognizer?.startListening(intent)
        _isListening.value = true
    }

    private fun stopListening() {
        speechRecognizer?.stopListening()
        _isListening.value = false
    }

    fun sendMessageToAI(message: String) {
        viewModelScope.launch {
            _chatMessages.update { current ->
                current + ChatMessage(message, isUser = true, id = UUID.randomUUID().toString())
            }

            _isTyping.value = true

            val response = repository.getChatResponse(message)

            _isTyping.value = false

            response.onSuccess { aiMessage ->
                _chatMessages.update { current ->
                    current + ChatMessage(aiMessage, isUser = false, id = UUID.randomUUID().toString())
                }
            }.onFailure {
                _chatMessages.update { current ->
                    current + ChatMessage("Failed to get response.", isUser = false, id = UUID.randomUUID().toString())
                }
            }
        }
    }

    fun readMessage(messageId: String, text: String) {
        if (!isTtsReady) {
            Log.d("ChatViewModel", "TTS not ready")
            return
        }
        stopReading() // Stop any ongoing speech
        _readingMessageId.value = messageId
        Log.d("ChatViewModel", "Reading message: $text")
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, messageId)
    }

    fun stopReading() {
        if (tts?.isSpeaking == true) {
            tts?.stop()
        }
        _readingMessageId.value = null
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
        tts?.shutdown()
    }
}