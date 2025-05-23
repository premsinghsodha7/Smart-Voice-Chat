package com.compose.smartvoicechat.model

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val id: String // Unique ID for each message (needed to track TTS)
)