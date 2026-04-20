package com.example.szlaki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.szlaki.TrailsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, vm: TrailsViewModel) {
    val typ = vm.selectedTab.value
    val trails by vm.trails.observeAsState(initial = emptyList())

    LaunchedEffect(typ) {
        vm.fetchTrails(typ)
    }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Szlaki") },
            actions = {
                IconButton(onClick = { navController.navigate("profile") }) {
                    Icon(Icons.Filled.Person, contentDescription = "Twój Profil")
                }
            }
        ) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Button(onClick = { vm.selectedTab.value = "gorskie" }) { Text("Górskie") }
                Button(onClick = { vm.selectedTab.value = "rowerowe" }) { Text("Rowerowe") }
            }

            val naglowek = if (typ == "gorskie") "Szlaki górskie" else "Szlaki rowerowe"
            Text(
                text = naglowek,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                if(trails.isEmpty()) {
                    item {
                        Text("Brak danych.", modifier = Modifier.padding(16.dp))
                    }
                } else {
                    items(trails) { trail ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    vm.selectedTrail.value = trail
                                    navController.navigate("details")
                                }
                        ) {
                            Text(trail.name, modifier = Modifier.padding(16.dp))
                        }
                    }
                }
            }
        }
    }
}