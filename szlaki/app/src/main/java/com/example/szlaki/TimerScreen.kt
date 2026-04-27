package com.example.szlaki

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerScreen(navController: NavController, trailsVm: TrailsViewModel, timerVm: TimerViewModel, authVm: AuthViewModel) {
    val trail = trailsVm.selectedTrail.value
    val trailName = trail?.name ?: "Nieznany szlak"
    val timersMap by timerVm.timers.collectAsState()
    val timerState = timersMap[trailName] ?: TrailTimerState()

    val time = timerState.timeMillis
    val isRecording = timerState.isRecording

    val user by authVm.currentUser.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trening na szlaku") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = trail?.name ?: "Nieznany szlak",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = timerVm.formatTime(time),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { timerVm.reset(trailName) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) {
                    Text("Reset")
                }

                Button(
                    onClick = { timerVm.toggle(trailName) },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text(if (isRecording) "Pauza" else "Start")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedButton(
                onClick = {
                    user?.login?.let { login ->
                        timerVm.saveRecord(
                            userLogin = login,
                            trailName = trailName,
                            trailType = trail?.type ?: "hiking"
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth(0.8f),
                enabled = time > 0L
            ) {
                Text("Zapisz czas w profilu")
            }
        }
    }
}