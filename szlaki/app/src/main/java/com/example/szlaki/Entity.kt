package com.example.szlaki
import androidx.room.*

@Entity
data class Trail(
    @PrimaryKey(autoGenerate = true) val trail_id: Int = 0,
    val name: String,
    val type: String,
    val surface: String,
    val difficulty: String,
    val color: String,
    val operator: String
)

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val user_id: Int = 0,
    val login: String,
    val password: String,
    val firstName: String,
    val lastName: String
)