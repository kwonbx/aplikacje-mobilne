package com.example.szlaki

import androidx.lifecycle.LiveData

class RecordRepository(private val dao: RecordDao) {
    suspend fun insertRecord(record: RecordEntity) {
        dao.insertRecord(record)
    }

    suspend fun deleteRecord(record: RecordEntity) {
        dao.deleteRecord(record)
    }

    fun getRecordsForUser(login: String): LiveData<List<RecordEntity>> {
        return dao.getRecordsForUser(login)
    }
}