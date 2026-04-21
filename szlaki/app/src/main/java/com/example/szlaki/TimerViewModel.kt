package com.example.szlaki

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class TimerViewModel: ViewModel() {
    private val _timeMillis = MutableLiveData(0L)
    val timeMillis: LiveData<Long> = _timeMillis

    private val _isRecording = MutableLiveData(false)
    val isRecording: LiveData<Boolean> = _isRecording

    private var timerJob: Job? = null

    fun toggle() {
        if (_isRecording.value == true) pause() else start()
    }

    private fun start() {
        _isRecording.value = true
        timerJob = viewModelScope.launch {
            while (isActive) {
                delay(100L)
                _timeMillis.value = (_timeMillis.value ?: 0L) + 100L
            }
        }
    }

    fun pause() {
        _isRecording.value = false
        timerJob?.cancel()
    }

    fun reset() {
        pause()
        _timeMillis.value = 0L
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }
}