package com.example.szlaki

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import coil.compose.AsyncImage

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

    val configuration = LocalConfiguration.current
    val isTablet = configuration.smallestScreenWidthDp >= 600
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val showCompactMenu = isLandscape && !isTablet

    var filterMenuExpanded by remember { mutableStateOf(false) }

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
                if (showCompactMenu) {
                    Box {
                        IconButton(onClick = { filterMenuExpanded = true }) {
                            Icon(Icons.Filled.FilterList, contentDescription = "Filtruj")
                        }
                        DropdownMenu(
                            expanded = filterMenuExpanded,
                            onDismissRequest = { filterMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Wszystkie szlaki") },
                                onClick = { vm.selectedTab.value = "wszystkie"; filterMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Szlaki Górskie") },
                                onClick = { vm.selectedTab.value = "gorskie"; filterMenuExpanded = false }
                            )
                            DropdownMenuItem(
                                text = { Text("Szlaki Rowerowe") },
                                onClick = { vm.selectedTab.value = "rowerowe"; filterMenuExpanded = false }
                            )
                        }
                    }
                }
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

             if(!showCompactMenu) {
                Column(modifier = Modifier.fillMaxWidth().padding(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        onClick = { vm.selectedTab.value = "wszystkie" },
                        modifier = Modifier.widthIn(max = 500.dp).fillMaxWidth().align(Alignment.CenterHorizontally),
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
                                .padding(vertical = 8.dp)
                                .clickable {
                                    onTrailClick(trail)
                                },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    model = trail.imageUrl,
                                    contentDescription = "Zdjęcie szlaku ${trail.name}",
                                    modifier = Modifier
                                        .size(80.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Spacer(modifier = Modifier.width(16.dp))

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
                                            tint = if (isFavorite) Color(0xFFCC5470) else MaterialTheme.colorScheme.outline
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