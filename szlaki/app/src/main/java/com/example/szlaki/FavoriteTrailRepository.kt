package com.example.szlaki

import androidx.lifecycle.LiveData

class FavoriteTrailRepository(private val dao: FavoriteTrailDao) {
    suspend fun addFavorite(login: String, trailName: String) {
        dao.insertFavorite(FavoriteTrailEntity(login, trailName))
    }

    suspend fun removeFavorite(login: String, trailName: String) {
        dao.removeFavorite(login, trailName)
    }

    fun getFavoriteTrailNames(login: String): LiveData<List<String>> = dao.getFavoriteTrailNames(login)

    fun getFavoriteTrails(login: String): LiveData<List<TrailEntity>> = dao.getFavoriteTrails(login)
}