package com.example.szlaki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, vm: TrailsViewModel, authVm: AuthViewModel, favVm: FavoritesViewModel, onTrailClick: (TrailEntity) -> Unit) {
    val typ = vm.selectedTab.value
    val trails by vm.trails.observeAsState(initial = emptyList())

    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    val user by authVm.currentUser.observeAsState()
    val login = user?.login ?: ""
    val favoriteNames by favVm.getFavoriteTrailNames(login).observeAsState(emptyList())

    LaunchedEffect(typ) {
        vm.fetchTrails(typ)
        searchQuery = ""
    }

    val filteredTrails = trails.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = { TopAppBar(
            title = {
                if (isSearchActive) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Szukaj szlaku...") },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text("Szlaki")
                }
            },
            actions = {
                if (isSearchActive) {
                    IconButton(onClick = {
                        if (searchQuery.isNotEmpty()) {
                            searchQuery = ""
                        } else {
                            isSearchActive = false
                        }
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Zamknij wyszukiwanie")
                    }
                } else {
                    IconButton(onClick = { isSearchActive = true }) {
                        Icon(Icons.Filled.Search, contentDescription = "Szukaj")
                    }
                    IconButton(onClick = { navController.navigate("profile") }) {
                        Icon(Icons.Filled.Person, contentDescription = "Twój Profil")
                    }
                }
            }
        ) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Button(
                    onClick = { vm.selectedTab.value = "wszystkie" },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Wszystkie", style = MaterialTheme.typography.labelSmall)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { vm.selectedTab.value = "gorskie" },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Hiking,
                            contentDescription = "Szlaki górskie"
                        )
                    }

                    Button(
                        onClick = { vm.selectedTab.value = "rowerowe" },
                        modifier = Modifier.weight(1f),
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.DirectionsBike,
                            contentDescription = "Szlaki rowerowe"
                        )
                    }
                }
            }

            val naglowek = when(typ) {
                "gorskie" -> "Szlaki górskie"
                "rowerowe" -> "Szlaki rowerowe"
                else -> "Wszystkie szlaki"
            }

            Text(
                text = naglowek,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                if(filteredTrails.isEmpty()) {
                    item {
                        Text(
                            text = if (trails.isEmpty()) "Brak danych." else "Nie znaleziono szlaku.",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    items(filteredTrails) { trail ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    onTrailClick(trail)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = trail.name,
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.bodyLarge
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (trail.type == "hiking") Icons.Filled.Hiking else Icons.AutoMirrored.Filled.DirectionsBike,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.secondary,
                                        modifier = Modifier.size(24.dp)
                                    )

                                    Spacer(modifier = Modifier.width(8.dp))

                                    val isFavorite = favoriteNames.contains(trail.name)

                                    IconButton(onClick = { favVm.toggleFavorite(login, trail.name, isFavorite) }) {
                                        Icon(
                                            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                            contentDescription = "Ulubione",
                                            tint = if (isFavorite) Color(0xFFE91E63) else MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}