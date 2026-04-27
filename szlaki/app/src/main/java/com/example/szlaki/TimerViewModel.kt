package com.example.szlaki

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive

data class TrailTimerState(
    val timeMillis: Long = 0L,
    val isRecording: Boolean = false
)

class TimerViewModel(private val repository: RecordRepository): ViewModel() {
    private val _timers = MutableStateFlow<Map<String, TrailTimerState>>(emptyMap())
    val timers: StateFlow<Map<String, TrailTimerState>> = _timers.asStateFlow()

    private val timerJobs = mutableMapOf<String, Job>()

    fun toggle(trailName: String) {
        val currentState = _timers.value[trailName] ?: TrailTimerState()
        if (currentState.isRecording) pause(trailName) else start(trailName)
    }

    private fun start(trailName: String) {
        _timers.update { map ->
            val state = map[trailName] ?: TrailTimerState()
            map + (trailName to state.copy(isRecording = true))
        }

        timerJobs[trailName]?.cancel()
        timerJobs[trailName] = viewModelScope.launch {
            while (isActive) {
                delay(100L)
                _timers.update { map ->
                    val state = map[trailName] ?: TrailTimerState()
                    map + (trailName to state.copy(timeMillis = state.timeMillis + 100L))
                }
            }
        }
    }

    fun pause(trailName: String) {
        _timers.update { map ->
            val state = map[trailName] ?: TrailTimerState()
            map + (trailName to state.copy(isRecording = false))
        }
        timerJobs[trailName]?.cancel()
    }

    fun reset(trailName: String) {
        pause(trailName)
        _timers.update { map ->
            map + (trailName to TrailTimerState(timeMillis = 0L, isRecording = false))
        }
    }

    @SuppressLint("DefaultLocale")
    fun formatTime(millis: Long): String {
        val seconds = (millis / 1000) % 60
        val minutes = (millis / (1000 * 60)) % 60
        val hours = (millis / (1000 * 60 * 60))
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    override fun onCleared() {
        super.onCleared()
        timerJobs.values.forEach { it.cancel() }
    }

    fun saveRecord(userLogin: String, trailName: String, trailType: String) {
        val state = timers.value[trailName] ?: return
        if (state.timeMillis == 0L) return

        viewModelScope.launch {
            val record = RecordEntity(
                userLogin = userLogin,
                trailName = trailName,
                trailType = trailType,
                dateMillis = System.currentTimeMillis(),
                timeMillis = state.timeMillis
            )
            repository.insertRecord(record)
            reset(trailName)
        }
    }

    class Factory(private val repository:   RecordRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TimerViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TimerViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}