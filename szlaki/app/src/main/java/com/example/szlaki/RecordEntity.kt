package com.example.szlaki

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trailRecords")
data class RecordEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userLogin: String,
    val trailName: String,
    val trailType: String,
    val dateMillis: Long,
    val timeMillis: Long
)