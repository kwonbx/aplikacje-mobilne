package com.example.szlaki

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteTrailDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteTrailEntity)

    @Query("DELETE FROM favorite_trails WHERE userLogin = :login AND trailName = :trailName")
    suspend fun removeFavorite(login: String, trailName: String)

    @Query("SELECT trailName FROM favorite_trails WHERE userLogin = :login")
    fun getFavoriteTrailNames(login: String): LiveData<List<String>>

    @Query("SELECT t.* FROM trails t INNER JOIN favorite_trails f ON t.name = f.trailName WHERE f.userLogin = :login")
    fun getFavoriteTrails(login: String): LiveData<List<TrailEntity>>
}