package com.fit2081.chuashengxin_32837933.nutritrack.data

import com.fit2081.chuashengxin_32837933.nutritrack.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content

class AIMessagesRepository(private val dao: MessagesDao) {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GENAI_API_KEY
    )

    suspend fun sendPrompt(prompt: String): String {
        val response = generativeModel.generateContent(
            content { text(prompt) }
        )
        return response.text ?: throw Exception("Empty AI response")
    }

    suspend fun insertMessage(text: String) {
        val message = AIMessage(messageText = text, timestamp = System.currentTimeMillis())
        dao.insertMessage(message)
    }

    suspend fun getAllMessages(): List<AIMessage> {
        return dao.getAllMessages()
    }

    suspend fun clearMessages() {
        dao.clearMessages()
    }


}
