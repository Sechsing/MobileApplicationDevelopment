package com.fit2081.chuashengxin_32837933.nutritrack.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
data class AIMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val messageText: String,
    val timestamp: Long
)
