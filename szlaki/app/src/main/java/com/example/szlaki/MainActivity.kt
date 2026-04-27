package com.example.szlaki

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.szlaki.ui.theme.SzlakiTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getInstance(this)
        val trailRepository = TrailRepository(database.trailDao())
        val userRepository = UserRepository(database.userDao())
        val recordRepository = RecordRepository(database.recordDao())
        val favRepository = FavoriteTrailRepository(database.favoriteTrailDao())
        val themeVm = ThemeViewModel()

        enableEdgeToEdge()
        setContent {
            val darkTheme by themeVm.isDarkTheme.observeAsState(initial = false)
            SzlakiTheme(darkTheme = darkTheme) {
                MyApp(trailRepository, userRepository, themeVm, recordRepository, favRepository)
            }
        }
    }
}

@Composable
fun MyApp(trailRepository: TrailRepository, userRepository: UserRepository, themeVm: ThemeViewModel, recordRepository: RecordRepository, favRepository: FavoriteTrailRepository) {
    val navController = rememberNavController()
    val trailsVm: TrailsViewModel = viewModel(factory = TrailsViewModel.Factory(trailRepository))
    val authVm: AuthViewModel = viewModel(factory = AuthViewModel.Factory(userRepository))
    val recordsVm: RecordsViewModel = viewModel(factory = RecordsViewModel.Factory(recordRepository))
    val timerVm: TimerViewModel = viewModel(factory = TimerViewModel.Factory(recordRepository))
    val favVm: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory(favRepository))

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController, authVm)
        }

        composable("register") {
            RegisterScreen(navController, authVm)
        }

        composable("home") {
            HomeScreen(navController, trailsVm, authVm, favVm)
        }

        composable("details") {
            DetailsScreen(navController, trailsVm)
        }

        composable("profile") {
            ProfileScreen(navController, authVm, themeVm)
        }

        composable("records") {
            RecordsScreen(navController, authVm, recordsVm)
        }

        composable("timer") {
            TimerScreen(navController, trailsVm, timerVm, authVm)
        }

        composable("favorites") {
            FavoritesScreen(navController, authVm, favVm, trailsVm)
        }
    }
}