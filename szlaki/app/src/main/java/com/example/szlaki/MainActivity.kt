package com.example.szlaki

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
        val sharedPreferences = getSharedPreferences("SzlakiPrefs", Context.MODE_PRIVATE)

        enableEdgeToEdge()
        setContent {
            val themeVm: ThemeViewModel = viewModel()
            val darkTheme by themeVm.isDarkTheme.observeAsState(initial = false)
            SzlakiTheme(darkTheme = darkTheme) {
                MyApp(trailRepository, userRepository, themeVm, recordRepository, favRepository, sharedPreferences)
            }
        }
    }
}

@Composable
fun MyApp(trailRepository: TrailRepository, userRepository: UserRepository, themeVm: ThemeViewModel, recordRepository: RecordRepository, favRepository: FavoriteTrailRepository, sharedPrefs: SharedPreferences) {
    val navController = rememberNavController()
    val trailsVm: TrailsViewModel = viewModel(factory = TrailsViewModel.Factory(trailRepository))
    val authVm: AuthViewModel = viewModel(factory = AuthViewModel.Factory(userRepository, sharedPrefs))
    val recordsVm: RecordsViewModel = viewModel(factory = RecordsViewModel.Factory(recordRepository))
    val timerVm: TimerViewModel = viewModel(factory = TimerViewModel.Factory(recordRepository))
    val favVm: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory(favRepository))

    val user by authVm.currentUser.observeAsState()

    val configuration = LocalConfiguration.current
    val isTablet = configuration.screenWidthDp >= 600

    LaunchedEffect(isTablet) {
        if (isTablet && navController.currentDestination?.route == "details") {
            navController.popBackStack("home", inclusive = false)
        }
    }

    LaunchedEffect(user) {
        if (user != null) {
            themeVm.setTheme(user!!.isDarkTheme)
        } else {
            themeVm.setTheme(false)
        }
    }

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }

        composable("login") {
            LoginScreen(navController, authVm)
        }

        composable("register") {
            RegisterScreen(navController, authVm)
        }

        composable("home") {
            if (isTablet) {
                Row(modifier = Modifier.fillMaxSize()) {
                    Box(modifier = Modifier.weight(1f)) {
                        HomeScreen(navController, trailsVm, authVm, favVm, onTrailClick = { trail ->
                            trailsVm.selectedTrail.value = trail
                        })
                    }

                    VerticalDivider()

                    Box(modifier = Modifier.weight(1f)) {
                        if (trailsVm.selectedTrail.value == null) {
                            Box(Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
                                Text(text = "Wybierz szlak z listy po lewej",
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge)
                            }
                        } else {
                            DetailsScreen(navController, trailsVm, isTablet = true)
                        }
                    }
                }
            } else {
                HomeScreen(navController, trailsVm, authVm, favVm, onTrailClick = { trail ->
                    trailsVm.selectedTrail.value = trail
                    navController.navigate("details")
                })
            }
        }

        composable("details") {
            DetailsScreen(navController, trailsVm, isTablet = false)
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