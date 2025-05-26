package com.fit2081.chuashengxin_32837933.nutritrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AIMessage::class], version = 1)
abstract class AIMessagesDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao

    companion object {
        @Volatile
        private var INSTANCE: AIMessagesDatabase? = null

        fun getDatabase(context: Context): AIMessagesDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AIMessagesDatabase::class.java,
                    "ai_messages_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
