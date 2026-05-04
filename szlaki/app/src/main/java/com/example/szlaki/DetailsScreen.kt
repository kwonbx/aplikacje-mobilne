package com.example.szlaki

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, vm: TrailsViewModel, isTablet: Boolean = false) {
    val trail = vm.selectedTrail.value
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły szlaku") },
                navigationIcon = {
                    if (!isTablet) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wróć")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("timer")
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
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
                    .padding(16.dp)
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(trail.name, style = MaterialTheme.typography.headlineMedium)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    SuggestionChip(
                        onClick = {},
                        label = { Text(if (trail.type == "hiking") "Pieszy" else "Rowerowy") }
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Kolor: ${trail.color.uppercase()}", fontWeight = FontWeight.Bold)
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                InfoRow(label = "Trudność", value = trail.difficulty ?: "Brak danych")
                InfoRow(label = "Nawierzchnia", value = trail.surface ?: "Naturalna/Brak danych")
                InfoRow(label = "Zarządca", value = trail.operator ?: "Lokalny")

                Spacer(Modifier.height(16.dp))

                val imageHeight = if (isTablet) 280.dp else 220.dp
                val imageWidth = if (isTablet) 600.dp else 400.dp

                AsyncImage(
                    model = trail.imageUrl,
                    contentDescription = "Szczegółowe zdjęcie szlaku",
                    modifier = Modifier
                        .width(imageWidth)
                        .height(imageHeight)
                        .clip(RoundedCornerShape(12.dp))
                        .align(Alignment.CenterHorizontally),
                    contentScale = ContentScale.Crop
                )
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