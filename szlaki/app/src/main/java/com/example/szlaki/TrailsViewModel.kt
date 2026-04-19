package com.example.szlaki

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.switchMap

class TrailsViewModel(private val repository: TrailRepository): ViewModel() {
    private val currentType = MutableLiveData<String>("hiking")

    val trails: LiveData<List<TrailEntity>> = currentType.switchMap { type -> repository.getTrailsByType(type) }

    val selectedTrail = mutableStateOf<TrailEntity?>(null)

    fun fetchTrails(type: String) {
        val dbType = if (type == "gorskie") "hiking" else "bicycle"
        currentType.value = dbType
    }

    class Factory(private val repository: TrailRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TrailsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TrailsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}