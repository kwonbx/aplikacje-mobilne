package com.example.szlaki

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecordsViewModel(private val repository: RecordRepository) : ViewModel() {

    fun getRecords(login: String): LiveData<List<RecordEntity>> {
        return repository.getRecordsForUser(login)
    }

    fun deleteRecord(record: RecordEntity) {
        viewModelScope.launch {
            repository.deleteRecord(record)
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    class Factory(private val repository: RecordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RecordsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return RecordsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}