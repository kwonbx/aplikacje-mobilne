package com.example.szlaki

class UserRepository(private val userDao: UserDao) {
    suspend fun registerUser(user: UserEntity): Boolean {
        val result = userDao.insertUser(user)
        return result != -1L
    }

    suspend fun loginUser(login: String, pass: String): UserEntity? {
        return userDao.getUser(login, pass)
    }

    suspend fun updateUserTheme(login: String, isDarkTheme: Boolean) {
        userDao.updateUserTheme(login, isDarkTheme)
    }

    suspend fun getUserByLogin(login: String): UserEntity? {
        return userDao.getUserByLogin(login)
    }
}