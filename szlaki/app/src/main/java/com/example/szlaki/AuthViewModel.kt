package com.example.szlaki

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.szlaki.UserEntity
import com.example.szlaki.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: UserRepository): ViewModel() {
    private val _message = MutableLiveData<String?>(null)
    val message: LiveData<String?> = _message

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _currentUser = MutableLiveData<UserEntity?>(null)
    val currentUser: LiveData<UserEntity?> = _currentUser

    fun login(login: String, pass: String) {
        viewModelScope.launch {
            val user = repository.loginUser(login, pass)
            if (user != null) {
                _currentUser.value = user
                _isLoggedIn.value = true
                _message.value = null
            } else {
                _message.value = "Nieprawidłowy login lub hasło."
            }
        }
    }

    fun logout() {
        _currentUser.value = null
        _isLoggedIn.value = false
    }

    fun register(login: String, pass: String, fName: String, lName: String, bDate: String) {
        if (login.isBlank() || pass.isBlank() || fName.isBlank() || lName.isBlank() || bDate.isBlank()) {
            _message.value = "Wypełnij wszystkie pola!"
            return
        }

        viewModelScope.launch {
            val success = repository.registerUser(UserEntity(login, pass, fName, lName, bDate))
            if (success) {
                _message.value = "Rejestracja udana! Możesz się zalogować."
            } else {
                _message.value = "Taki login już istnieje!"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }

    fun updateThemePreference(isDark: Boolean) {
        val user = _currentUser.value ?: return

        viewModelScope.launch {
            repository.updateUserTheme(user.login, isDark)
            _currentUser.value = user.copy(isDarkTheme = isDark)
        }
    }

    class Factory(private val repository: UserRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
               @Suppress("UNCHECKED_CAST")
               return AuthViewModel(repository) as T
            }
        throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
