package com.fit2081.chuashengxin_32837933.nutritrack.data

import kotlinx.coroutines.flow.Flow

class UsersRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User) {
        userDao.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    suspend fun getUserByPhone(phoneNumber: String): User? {
        return userDao.getUserByPhone(phoneNumber)
    }

    suspend fun authenticateUser(userId: Int, password: String): User? {
        return userDao.authenticateUser(userId, password)
    }

    fun getAllUserIds(): Flow<List<String>> {
        return userDao.getAllUserIds()
    }

    fun getAllUsers(): Flow<List<User>> {
        return userDao.getAllUsers()
    }
}
