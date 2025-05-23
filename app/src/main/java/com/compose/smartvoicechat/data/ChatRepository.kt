package com.compose.smartvoicechat.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class ChatRepository {

    private val apiKey = "sk-YOUR_API_KEY" // <-- Replace this
    private val apiUrl = "https://openrouter.ai/api/v1/chat/completions"

    suspend fun getChatResponse(userMessage: String): Result<String> = withContext(Dispatchers.IO) {
        return@withContext try {
            val url = URL(apiUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $apiKey")
            conn.doOutput = true

            val requestBody = JSONObject().apply {
                put("model", "mistralai/mistral-7b-instruct") // or "openai/gpt-3.5-turbo"
                put("messages", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("content", userMessage)
                    })
                })
            }

            conn.outputStream.use { os ->
                os.write(requestBody.toString().toByteArray())
                os.flush()
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = conn.inputStream.bufferedReader().use(BufferedReader::readText)
                val jsonResponse = JSONObject(response)
                val message = jsonResponse
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
                Result.success(message.trim())
            } else {
                val error = conn.errorStream?.bufferedReader()?.use(BufferedReader::readText)
                Log.e("ChatRepository", "API Error: $responseCode $error")
                Result.failure(Exception("API Error: $responseCode"))
            }
        } catch (e: Exception) {
            Log.e("ChatRepository", "Exception: ${e.message}", e)
            Result.failure(e)
        }
    }
}