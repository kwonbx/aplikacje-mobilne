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

data class OverpassResponse(val elements: List<TrailElement>)
data class TrailElement(val id: Long, val tags: Map<String, String>?) {
    val name: String get() = tags?.get("name") ?: "Szlak bez nazwy"
    val type: String get() = tags?.get("route") ?: "nieznany"

    val surface: String? get() = tags?.get("surface")
    val difficulty: String? get() = tags?.get("sac_scale") ?: tags?.get("mtb:scale")
    val color: String? get() = tags?.get("osmc:symbol")?.split(":")?.getOrNull(1)
    val network: String? get() = tags?.get("network")
    val operator: String? get() = tags?.get("operator")
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
    var selectedTrail = mutableStateOf<TrailElement?>(null)

    fun fetchTrails(type: String) {
        val osmTag = if (type == "gorskie") "hiking" else "bicycle"
        val query = """
            [out:json];
            relation["route"="$osmTag"](49.1,19.7,49.3,20.2);
            out body;
        """.trimIndent()

        val scope = viewModelScope
        isLoading.value = true
        scope.launch {
            try {
                val response = RetrofitClient.api.getTrails(query)
                trails.value = response.elements.filter { it.tags?.containsKey("name") == true }
            } catch (e: Exception) { }
            finally {
                isLoading.value = false
            }
        }
    }
}

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

@Composable
fun MyApp() {
    val navController = rememberNavController()
    val vm: TrailsViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController, vm)
        }

        composable("details") {
            DetailsScreen(navController, vm)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, vm: TrailsViewModel = viewModel()) {
    var typ by remember { mutableStateOf("gorskie") }

    // Pobierz dane przy starcie lub zmianie typu
    LaunchedEffect(typ) {
        vm.fetchTrails(typ)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Szlaki") }) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                Button(onClick = { typ = "gorskie" }) { Text("Górskie") }
                Button(onClick = { typ = "rowerowe" }) { Text("Rowerowe") }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(navController: NavController, vm: TrailsViewModel) {
    val trail = vm.selectedTrail.value

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Szczegóły szlaku") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("<")
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
                        Text("Kolor: ${trail.color?.uppercase()}", fontWeight = FontWeight.Bold)
                    }
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                InfoRow(label = "Trudność", value = trail.difficulty ?: "Brak danych")
                InfoRow(label = "Nawierzchnia", value = trail.surface ?: "Naturalna/Brak danych")
                InfoRow(label = "Zarządca", value = trail.operator ?: "Lokalny")
                InfoRow(label = "Ranga", value = translateNetwork(trail.network))
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

fun translateNetwork(network: String?): String {
    return when (network) {
        "nwn" -> "Międzynarodowy"
        "rwn" -> "Regionalny"
        "lwn" -> "Lokalny"
        else -> "Inny"
    }
}