package com.fit2081.chuashengxin_32837933.nutritrack.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT * FROM users WHERE userId = :userId")
    suspend fun getUserById(userId: Int): User?

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber")
    suspend fun getUserByPhone(phoneNumber: String): User?

    @Query("SELECT * FROM users WHERE userId = :userId AND password = :password")
    suspend fun authenticateUser(userId: Int, password: String): User?

    @Query("SELECT CAST(userId AS TEXT) FROM users")
    fun getAllUserIds(): Flow<List<String>>

    @Query("SELECT * FROM users")
    fun getAllUsers(): Flow<List<User>>
}
