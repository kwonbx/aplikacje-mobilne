package com.example.szlaki

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity): Long

    @Query("Select * from users where login = :login and password = :password")
    suspend fun getUser(login: String, password: String): UserEntity?

    @Query("UPDATE users SET isDarkTheme = :isDarkTheme WHERE login = :login")
    suspend fun updateUserTheme(login: String, isDarkTheme: Boolean)
}