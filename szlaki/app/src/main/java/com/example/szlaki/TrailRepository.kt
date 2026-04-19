package com.example.szlaki

import androidx.lifecycle.LiveData

class TrailRepository(private val trailDao: TrailDao) {
    fun getTrailsByType(type: String): LiveData<List<TrailEntity>> {
        return trailDao.getTrailsByType(type)
    }

    suspend fun insertTrails(trails: List<TrailEntity>) {
        trailDao.insertTrails(trails)
    }
}