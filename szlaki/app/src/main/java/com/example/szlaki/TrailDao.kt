package com.example.szlaki
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TrailDao {
    @Query("Select * from trails where type = :type")
    fun getTrailsByType(type: String): LiveData<List<TrailEntity>>

    @Query("Select * from trails")
    fun getAllTrails(): LiveData<List<TrailEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrails(trails: List<TrailEntity>)
}