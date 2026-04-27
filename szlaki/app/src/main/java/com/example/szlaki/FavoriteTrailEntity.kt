package com.example.szlaki

import androidx.room.Entity

@Entity(tableName = "favorite_trails", primaryKeys = ["userLogin", "trailName"])
data class FavoriteTrailEntity(
    val userLogin: String,
    val trailName: String
)