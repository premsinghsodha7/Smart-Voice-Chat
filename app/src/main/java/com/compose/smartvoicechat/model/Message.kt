package com.compose.smartvoicechat.model

data class Message(
    val role: String, // "user" or "assistant"
    val content: String
)
