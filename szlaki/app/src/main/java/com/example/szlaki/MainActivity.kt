package com.example.szlaki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.*
import com.example.szlaki.ui.theme.SzlakiTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Timer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val repository = TrailRepository(database.trailDao())

        lifecycleScope.launch(Dispatchers.IO) {
            val dbTrails = repository.getTrailsByType("hiking")
            // Sprawdzamy czy baza jest pusta z małym uproszczeniem
            repository.insertTrails(
                listOf(
                    TrailEntity(name = "Szlak na Rysy", type = "hiking", color = "red", difficulty = "hard", surface = "rock", operator = "TPN"),
                    TrailEntity(name = "Dolina Chochołowska", type = "hiking", color = "green", difficulty = "easy", surface = "gravel", operator = "TPN"),
                    TrailEntity(name = "Velo Dunajec", type = "bicycle", color = "blue", difficulty = "medium", surface = "asphalt", operator = "Małopolska")
                )
            )
        }

        enableEdgeToEdge()
        setContent {
            SzlakiTheme {
                MyApp(repository)
            }
        }
    }
}

@Composable
fun MyApp(repository: TrailRepository) {
    val navController = rememberNavController()
    val vm: TrailsViewModel = viewModel(
        factory = TrailsViewModel.Factory(repository)
    )

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, vm)
        }

        composable("details") {
            DetailsScreen(navController, vm)
        }
    }
}