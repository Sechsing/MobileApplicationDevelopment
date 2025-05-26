package com.fit2081.chuashengxin_32837933.nutritrack.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MessagesDao {
    @Insert
    suspend fun insertMessage(message: AIMessage)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC")
    suspend fun getAllMessages(): List<AIMessage>

    @Query("DELETE FROM messages")
    suspend fun clearMessages()
}
