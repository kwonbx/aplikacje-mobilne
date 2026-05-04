package com.example.szlaki
import androidx.room.*

@Entity(tableName = "trails")
data class TrailEntity(
    @PrimaryKey val name: String,
    val type: String,
    val surface: String,
    val difficulty: String,
    val color: String,
    val operator: String,
    val imageUrl: String? = null
)