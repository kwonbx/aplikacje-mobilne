package com.example.szlaki

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ThemeViewModel : ViewModel() {
    private val _isDarkTheme = MutableLiveData(false)
    val isDarkTheme: LiveData<Boolean> = _isDarkTheme

    fun setTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }
}