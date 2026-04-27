package com.example.szlaki

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Insert
    suspend fun insertRecord(record: RecordEntity)

    @Delete
    suspend fun deleteRecord(record: RecordEntity)

    @Query("SELECT * FROM trailRecords WHERE userLogin = :login ORDER BY dateMillis DESC")
    fun getRecordsForUser(login: String): LiveData<List<RecordEntity>>
}