package com.example.szlaki

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.szlaki.TrailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, vm: TrailsViewModel) {
    val trail = vm.selectedTrail.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły szlaku") },
                navigationIcon = {
                    Button(onClick = { navController.popBackStack() }) {
                        Text("<")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Twoja akcja stoper */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.Timer, contentDescription = "Stoper")
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { padding ->
        if (trail == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Nie znaleziono danych")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(trail.name, style = MaterialTheme.typography.headlineMedium)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(if (trail.type == "hiking") "Pieszy" else "Rowerowy") }
                    )
                    Spacer(Modifier.width(8.dp))
                    if (trail.color != null) {
                        Text("Kolor: ${trail.color.uppercase()}", fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                InfoRow(label = "Trudność", value = trail.difficulty ?: "Brak danych")
                InfoRow(label = "Nawierzchnia", value = trail.surface ?: "Naturalna/Brak danych")
                InfoRow(label = "Zarządca", value = trail.operator ?: "Lokalny")
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = MaterialTheme.colorScheme.secondary)
        Text(value, fontWeight = FontWeight.Medium)
    }
}