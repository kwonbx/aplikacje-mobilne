package com.example.szlaki

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class FavoritesViewModel(private val repository: FavoriteTrailRepository) : ViewModel() {

    fun toggleFavorite(login: String, trailName: String, isCurrentlyFavorite: Boolean) {
        viewModelScope.launch {
            if (isCurrentlyFavorite) {
                repository.removeFavorite(login, trailName)
            } else {
                repository.addFavorite(login, trailName)
            }
        }
    }

    fun getFavoriteTrailNames(login: String): LiveData<List<String>> {
        return repository.getFavoriteTrailNames(login)
    }

    fun getFavoriteTrails(login: String): LiveData<List<TrailEntity>> {
        return repository.getFavoriteTrails(login)
    }

    class Factory(private val repository: FavoriteTrailRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavoritesViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}