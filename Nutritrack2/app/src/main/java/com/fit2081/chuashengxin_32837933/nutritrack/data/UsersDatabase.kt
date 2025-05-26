package com.fit2081.chuashengxin_32837933.nutritrack.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 5, exportSchema = false)
abstract class UsersDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: UsersDatabase? = null

        fun getDatabase(context: Context): UsersDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    UsersDatabase::class.java,
                    "users_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}
