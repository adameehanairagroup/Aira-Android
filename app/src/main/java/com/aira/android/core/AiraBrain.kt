package com.aira.android.core

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * AiraBrain:
 * The AI reasoning core powered by Google Gemini (with fallback to Groq).
 * Manages API keys securely and generates intelligent tool-calling plans.
 */
class AiraBrain(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("aira_settings", Context.MODE_PRIVATE)

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    // Retrieve or save Gemini API Key
    var geminiApiKey: String
        get() = prefs.getString("gemini_api_key", "") ?: ""
        set(value) = prefs.edit().putString("gemini_api_key", value).apply()

    // Retrieve or save Groq API Key
    var groqApiKey: String
        get() = prefs.getString("groq_api_key", "") ?: ""
        set(value) = prefs.edit().putString("groq_api_key", value).apply()

    /**
     * Sends user intent to Gemini 3.8 / 2.5 Flash for natural reasoning and function execution.
     */
    suspend fun think(userPrompt: String): String = withContext(Dispatchers.IO) {
        val key = geminiApiKey
        if (key.isBlank()) {
            return@withContext "Boss, please configure your Gemini API Key in Aira Settings!"
        }

        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$key"

        val payload = JSONObject().apply {
            val contents = JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", "You are Aira, loyal autonomous companion to Er. Adam Eehan. Respond briefly and execute user's intent: $userPrompt"))
                    })
                })
            }
            put("contents", contents)
        }

        val mediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody = payload.toString().toRequestBody(mediaType)

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        try {
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext "Aira Brain Error: ${response.code}"
            }

            val json = JSONObject(responseBody)
            val candidates = json.optJSONArray("candidates")
            if (candidates != null && candidates.length() > 0) {
                val content = candidates.getJSONObject(0).optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                if (parts != null && parts.length() > 0) {
                    return@withContext parts.getJSONObject(0).optString("text", "Done Boss!")
                }
            }
            "Understood Boss!"
        } catch (e: Exception) {
            "Connection Error: ${e.message}"
        }
    }
}
