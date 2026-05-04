package com.example.szlaki

import androidx.room.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val login: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val birthDate: String,
    val isDarkTheme: Boolean = false
)