package com.example.szlaki

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(navController: NavController, authVm: AuthViewModel, favVm: FavoritesViewModel, trailsVm: TrailsViewModel) {
    val user by authVm.currentUser.observeAsState()
    val login = user?.login ?: ""
    val favoriteTrails by favVm.getFavoriteTrails(login).observeAsState(emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ulubione szlaki") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        }
    ) { padding ->
        if (favoriteTrails.isEmpty()) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Brak ulubionych szlaków.")
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                items(favoriteTrails) { trail ->
                    Card(
                        modifier = Modifier
                            .widthIn(max = 700.dp)
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                trailsVm.selectedTrail.value = trail
                                navController.navigate("details")
                            },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
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

                            Text(text = trail.name, modifier = Modifier.weight(1f))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (trail.type == "hiking") Icons.Filled.Terrain else Icons.AutoMirrored.Filled.DirectionsBike,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(onClick = { favVm.toggleFavorite(login, trail.name, true) }) {
                                    Icon(Icons.Filled.Favorite, contentDescription = "Usuń z ulubionych", tint = Color(0xFFCC5470))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}