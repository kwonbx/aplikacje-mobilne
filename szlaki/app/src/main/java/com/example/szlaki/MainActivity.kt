package com.example.szlaki

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.szlaki.ui.theme.SzlakiTheme
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SzlakiTheme {
                MyApp()
            }
        }
    }
}

data class OverpassResponse(val elements: List<TrailElement>)
data class TrailElement(
    val id: Long,
    val tags: Map<String, String>?
) {
    val name: String = tags?.get("name") ?: "Szlak bez nazwy"
    val distance: String = tags?.get("distance") ?: "Nie podano"

    // Mapowanie trudności (SAC Scale dla pieszych lub MTB Scale dla rowerów)
    val difficulty: String = when {
        tags?.containsKey("sac_scale") == true -> mapSacScale(tags["sac_scale"])
        tags?.containsKey("mtb:scale") == true -> "MTB Poziom: ${tags["mtb:scale"]}"
        else -> "Brak danych o trudności"
    }

    val color: String = tags?.get("osmc:symbol")?.split(":")?.getOrNull(1) ?: "nieokreślony"

    private fun mapSacScale(scale: String?): String = when(scale) {
        "hiking" -> "Bardzo łatwy (spacerowy)"
        "mountain_hiking" -> "Łatwy górski"
        "demanding_mountain_hiking" -> "Średnio trudny"
        "alpine_hiking" -> "Trudny (wymagający)"
        else -> "Standardowy"
    }
}

interface OverpassApi {
    @GET("interpreter")
    suspend fun getTrails(@Query("data") query: String): OverpassResponse
}

object RetrofitClient {
    val api: OverpassApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OverpassApi::class.java)
    }
}

class TrailsViewModel : ViewModel() {
    var trails = mutableStateOf<List<TrailElement>>(emptyList())
    var isLoading = mutableStateOf(false)

    // Przechowujemy wybrany obiekt, aby DetailsScreen mógł go wyświetlić
    var selectedTrail by mutableStateOf<TrailElement?>(null)

    fun fetchTrails(type: String) {
        val osmTag = if (type == "gorskie") "hiking" else "bicycle"
        val query = """
            [out:json];
            relation["route"="$osmTag"](49.1,19.7,49.3,20.2);
            out body;
        """.trimIndent()

        isLoading.value = true

        val launch = viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getTrails(query)
                trails.value = response.elements.filter { it.tags?.containsKey("name") == true }
                Log.d("API_SUCCESS", "Pobrano ${trails.value.size} szlaków")
            } catch (e: Exception) {
                // To uratuje Cię przed crashem - zamiast wyłączyć apkę, wypisze błąd
                Log.e("API_ERROR", "Błąd pobierania: ${e.message}")
                e.printStackTrace()
            } finally {
                isLoading.value = false
            }
        }
    }
}

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val sharedViewModel: TrailsViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, sharedViewModel) }
        composable("details") { DetailsScreen(navController, sharedViewModel) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, vm: TrailsViewModel = viewModel()) {
    var typ by remember { mutableStateOf("") }

    LaunchedEffect(typ) {
        vm.fetchTrails(typ)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text("Szlaki") })
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Button(onClick = { typ = "gorskie" }) {
                    Text("Górskie")
                }
                Button(onClick = { typ = "rowerowe" }) {
                    Text("Rowerowe")
                }
            }
            if (vm.isLoading.value) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    items(vm.trails.value) { trail ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    vm.selectedTrail = trail // Ustawiamy wybrany szlak w ViewModelu
                                    navController.navigate("details") // Nawigujemy bez parametrów w URL
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, vm: TrailsViewModel) {
    val trail = vm.selectedTrail

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły szlaku") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("< Powrót") // Lub ikona ArrowBack
                    }
                }
            )
        }
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
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(text = trail.name, style = MaterialTheme.typography.headlineMedium)

                Divider()

                // Karta z informacjami
                InfoRow(label = "Długość szlaku", value = trail.distance)
                InfoRow(label = "Trudność", value = trail.difficulty)
                InfoRow(label = "Kolor oznaczenia", value = trail.color)

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Dane pochodzą z OpenStreetMap (Overpass API). " +
                            "Pamiętaj, aby zawsze sprawdzać warunki na szlaku przed wyruszeniem.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
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
        Text(text = label, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(text = value)
    }
}