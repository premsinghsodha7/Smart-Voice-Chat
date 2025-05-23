package com.compose.smartvoicechat.ui.screen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
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

class ChatViewModel(
    private val repository: ChatRepository = ChatRepository()
) : ViewModel() {

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening

    private val _isTyping = MutableStateFlow(false)
    val isTyping: StateFlow<Boolean> = _isTyping

    private var speechRecognizer: SpeechRecognizer? = null

    fun onMicTapped(context: Context) {
        if (_isListening.value) {
            stopListening()
        } else {
            startListening(context)
        }
    }

    private fun startListening(context: Context) {
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
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
                Log.e("SpeechRecognizer", "Error: $error")
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
                current + ChatMessage(message, isUser = true)
            }

            _isTyping.value = true

            val result = repository.getChatResponse(message)

            _isTyping.value = false

            result.onSuccess { aiReply ->
                _chatMessages.update { current ->
                    current + ChatMessage(aiReply, isUser = false)
                }
            }.onFailure { error ->
                Log.e("ChatViewModel", "Error: ${error.message}")
                _chatMessages.update { current ->
                    current + ChatMessage("Error: ${error.message}", isUser = false)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        speechRecognizer?.destroy()
    }
}